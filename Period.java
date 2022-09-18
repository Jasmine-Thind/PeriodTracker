import java.time.LocalDate;

public class Period implements Comparable<Period> {
    private LocalDate startDate;
    private int length = 0;

    // Length is inclusive of both start and end.
    public Period(LocalDate startDate, int length) {
        this.startDate = startDate;
        this.length = length;
    }

    // Date is in the form YYYY-MM-DD
    public Period(String startDate, int length) {
        this.startDate = TrackerSystem.extractDate(startDate);
        this.length = length;
    }

    public Period(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.length = calculate_length(endDate);
    }

    // This method is for when the last day of the period was yesterday.
    public int calculate_length() {
        LocalDate today = LocalDate.now(); // subtract 1 day from this.
        LocalDate yesterday = today.minusDays(1);
        return daysPassed(startDate, yesterday);
    }

    // endDate is the last day of the period.
    public int calculate_length(LocalDate endDate) {
        return daysPassed(startDate, endDate);
    }


    // Returns the last day of the period
    public LocalDate getEndDate() {
        return startDate.plusDays(length).minusDays(1);
    }

    public static int daysPassed(LocalDate first_date, LocalDate second_date) {
        // Inclusive of start and end dates.

        // If the second date comes before the first date
        if (second_date.compareTo(first_date) < 0) {
            LocalDate x = first_date;
            first_date = second_date;
            second_date = x;
        }

        if (first_date.getYear() == second_date.getYear()) {
            return second_date.getDayOfYear() - first_date.getDayOfYear() + 1;
        }

        else {
            int daysPassed = 0;
            for (int i = first_date.getYear() + 1; i < second_date.getYear(); i++) {
                LocalDate temp = LocalDate.ofYearDay(i, 1);
                daysPassed += temp.lengthOfYear();
            }
            daysPassed += first_date.lengthOfYear() - first_date.getDayOfYear() + 1;
            daysPassed += second_date.getDayOfYear();
            return daysPassed;
        }
    }

    public void add(Period period) {
        this.length = daysPassed(this.startDate, period.getEndDate());
    }

    public String toString() {
        return "Period beginning " + startDate + " for " + length + " days.";
    }

    public boolean equals(Period period2) {
        return this.startDate == period2.startDate;
    }

    public int compareTo(Period period2) {
        return this.startDate.compareTo(period2.startDate);
    }

}

