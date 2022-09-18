import java.time.LocalDate;
import java.util.Scanner;

public class TrackerUserInterface {
    public static void main(String[] args) {
        TrackerSystem system = new TrackerSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.print(">");

        while (scanner.hasNextLine()) {
            try {
                String action = scanner.nextLine();

                // Log a period that started today.
                if (action.equalsIgnoreCase("LOGSTART")) {
                    system.logPeriod();
                    System.out.print("Successfully logged today as the start date of your period.");
                }

                // Predict the start date of the next period.
                else if (action.equalsIgnoreCase("NEXT")) {
                    LocalDate nextStart = system.next();
                    System.out.println("Your next period should start: " + nextStart);
                }

                // Save all the data and exit the program.
                else if (action.equalsIgnoreCase("SAVEANDQUIT") || (action.equalsIgnoreCase("QUIT") ||
                        (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("EXIT")))) {

                    system.save();
                    return;
                }

                // Log the end date of the latest period.
                else if (action.equalsIgnoreCase("LOGEND")) {
                    String endDate = "";
                    System.out.print("End date (YYYY-MM-DD): ");
                    if (scanner.hasNextLine()) {
                        endDate = scanner.nextLine();
                    }

                    system.logEnd(endDate);

                }

                // Log yesterday as the last day of the period.
                else if (action.equalsIgnoreCase("LOGENDYESTERDAY")) {
                    system.logEnd(LocalDate.now().minusDays(1));
                    System.out.print("Logged yesterday as the last day of your period.");
                }

                // Log an old period.
                else if (action.equalsIgnoreCase("LOGOLD")) {
                    String startDate = "";
                    String endDate = "";

                    System.out.print("Start date (YYYY-MM-DD): ");
                    if (scanner.hasNextLine()) {
                        startDate = scanner.nextLine();
                    }

                    System.out.print("\nEnd date (YYYY-MM-DD): ");
                    if (scanner.hasNextLine()) {
                        endDate = scanner.nextLine();
                    }

                    system.logOldPeriod(startDate, endDate);
                }

                // Print a list of all recorded periods.
                else if (action.equalsIgnoreCase("VIEW")) {
                    system.view();
                }
            }

            catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.print("\n>");
        }
    }
}
