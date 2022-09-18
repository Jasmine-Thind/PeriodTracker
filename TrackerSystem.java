import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class TrackerSystem {
    private Cycle cycle;

    public TrackerSystem() {
        try {
            cycle = readCycle();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public Cycle getCycle() {
        return cycle;
    }

    // Read the period data from the file.
    private Cycle readCycle() throws IOException {
        Cycle cycle = new Cycle();
        File file = new File(cycle.getFilename());
        file.createNewFile();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String date = scanner.nextLine();
            int length = Integer.parseInt(scanner.nextLine());
            Scanner dateScanner = new Scanner(date);
            dateScanner.useDelimiter("-|\\n");
            int year = Integer.parseInt(dateScanner.next());
            int month = Integer.parseInt(dateScanner.next());
            int day = Integer.parseInt(dateScanner.next());

            LocalDate startDate = LocalDate.of(year, month, day);

            Period currentPeriod = new Period(startDate, length);
            cycle.addPeriod(currentPeriod);
        }
        return cycle;
    }

    public void logPeriod() {
        LocalDate today = LocalDate.now();
        Period new_period = new Period(today);
        cycle.addPeriod(new_period);
    }

    public void logOldPeriod(String startDate, String endDate) throws InvalidDateException {
        LocalDate start = extractDate(startDate);
        LocalDate end = extractDate(endDate);
        int length = Period.daysPassed(start, end);

        if (length < 1 || length > 400) {
            throw new NumberFormatException("Invalid Period Length.");
        }

        Period period = new Period(start, length);
        cycle.addPeriod(period);
    }

    // Verifies that a date is valid and withing a reasonable timeframe.
    // Returns the LocalDate of a String date.
    public static LocalDate extractDate(String date) throws InvalidDateException {
        int year = 0;
        int month = 0;
        int day = 0;

        if (date.length() != 10) {
            throw new InvalidDateException("Invalid Date.");
        }

        Scanner dateReader = new Scanner(date);
        dateReader.useDelimiter("-");
        try {
            year = Integer.parseInt(dateReader.next());
            month = Integer.parseInt(dateReader.next());
            day = Integer.parseInt(dateReader.next());
        }
        catch (NumberFormatException e) {
            throw new InvalidDateException("Invalid Date.");
        }

        if (year < 1800 || year > 2100) {
            throw new InvalidDateException("Invalid year.");
        }
        if (month < 0 || month > 12) {
            throw new InvalidDateException("Invalid month.");
        }
        LocalDate temp = LocalDate.of(year, month, 1);
        if (day < 0 || day > temp.lengthOfMonth()) {
            throw new InvalidDateException("Invalid day.");
        }
        return LocalDate.of(year, month, day);
    }

    // Write all the periods in the Cycle to the file.
    public void save() throws IOException {
        PrintWriter writer = new PrintWriter(cycle.getFilename());

        for (Period period : cycle.getPeriods()) {
            writer.append(period.getStartDate().toString()).append("\n");
            writer.append(String.valueOf(period.getLength())).append("\n");
        }
        writer.close();
    }

    public void logEnd(String endDate) {
        LocalDate end = extractDate(endDate);
        Period period = cycle.getLatest();
        checkEndDate(end, period);
        period.setEndDate(end);
    }

    private void checkEndDate(LocalDate endDate, Period period) throws InvalidDateException {
        if (endDate.compareTo(period.getStartDate()) < 0) {
            throw new InvalidDateException("Invalid end date: End date is earlier than start date.");
        }
    }

    public void logEnd(LocalDate endDate) {
        Period period = cycle.getLatest();
        checkEndDate(endDate, period);
        period.setEndDate(endDate);
    }

    public void view() {
        System.out.print(cycle);
    }

    public LocalDate next() {

        LocalDate lastPeriodStart = cycle.getLatest().getStartDate();
        int cycleLength = cycle.measureCycle();
        return lastPeriodStart.plusDays(cycleLength);
    }

}

class InvalidDateException extends RuntimeException {
    public InvalidDateException() {
        super();
    }

    public InvalidDateException(String errorMessage) {
        super(errorMessage);

    }
}