import java.time.LocalDate;
import java.util.ArrayList;

public class Cycle {
    private ArrayList<Period> periods;
    private final String filename;

    public Cycle() {
        this.periods = new ArrayList<Period>();
        this.filename = "periodData.txt";
    }

    public Cycle(ArrayList<Period> periods, String filename) {
        this.periods = periods;
        this.filename = filename;
    }

    public Cycle(ArrayList<Period> periods) {
        this.periods = periods;
        this.filename = "periodData.txt";
    }

    public String getFilename() {
        return this.filename;
    }

    // Calculates the average cycle length.
    public int measureCycle() {
        int periodNumber = periods.size();
        int cycleLength = 0;

        if (periodNumber <= 1) {
            throw new LimitedDataException("Not enough data.");
        }

        if (periodNumber == 2) {
            // Days passed from first period to second period.
            // Subtract 1 day from the start of the second period because daysPassed is inclusive.
            cycleLength = Period.daysPassed(periods.get(0).getStartDate(), periods.get(1).getStartDate().minusDays(1));
        }

        if (periodNumber == 3) {
            int oneAndTwoGap = Period.daysPassed(periods.get(0).getStartDate(), periods.get(1).getStartDate().minusDays(1));
            int twoAndThreeGap = Period.daysPassed(periods.get(1).getStartDate(), periods.get(2).getStartDate().minusDays(1));
            cycleLength = (oneAndTwoGap + twoAndThreeGap) / 2;
        }

        if (periodNumber >= 4) {
            final int sampleSize = 4;
            int[] lengths = new int[sampleSize - 1];
            int index = 0;

            for (int i = periodNumber - 1; i > periodNumber - sampleSize; i--) {
                lengths[index] = Period.daysPassed(periods.get(i - 1).getStartDate(), periods.get(i).getStartDate().minusDays(1));
                index++;
            }

            for (Integer gap : lengths) {
                cycleLength += gap;
            }

            cycleLength = cycleLength / (sampleSize - 1);
        }
        return cycleLength;
    }

    public void addPeriod(Period period) {
        this.periods.add(period);
    }

    public ArrayList<Period> getPeriods() {
        return periods;
    }

    public Period getLatest() {
        return periods.get(periods.size() - 1);
    }


    // Given 2 periods, check if there is any overlap.
    // Return 1 if the second period starts during the first period (keep first)
    // Return 2 if the first period starts during the second period (keep second)
    // Return 0 if there is no overlap
    private static int checkOngoing(Period p1, Period p2) {
        LocalDate p1Start = p1.getStartDate();
        LocalDate p1End = p1.getEndDate();
        LocalDate p2Start = p2.getStartDate();
        LocalDate p2End = p2.getEndDate();

        // Check for direct overlap (one starts During the other).
        if (p2Start.compareTo(p1Start) >= 0 && p2Start.compareTo(p1End) <= 0) {
            return 1;
        }
        if (p1Start.compareTo(p2Start) >= 0 && p1Start.compareTo(p2End) <= 0) {
            return 2;
        }

        // The gap is 0 or 1 days (days passed is 2 or 3)
        if (Period.daysPassed(p1End, p2Start) <= 3) {
            return 1;
        }
        if (Period.daysPassed(p2End, p1Start) <= 3) {
            return 2;
        }

        return 0;
    }

    public String toString() {
        String string = "";
        for (int i = periods.size() - 1; i >= 0; i--) {
            string += (periods.size() - i) + ". " + periods.get(i).toString() + "\n";
        }
        return string;
    }
}

class LimitedDataException extends RuntimeException {
    public LimitedDataException() {
        super();
    }

    public LimitedDataException(String errorMessage) {
        super(errorMessage);
    }
}
