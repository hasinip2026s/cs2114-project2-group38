import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

public class InputValidator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public static String readName(Scanner in) {
        while (true) {
            System.out.print("Task name: ");
            String name = in.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("The task name can't be empty");
            } else if (name.contains("|")) {
                System.out.println("The task name can't contain |");
            } else {
                return name;
            }
        }
    }
    public static boolean readYesNo(Scanner in) {
        while (true) {
            String answer = in.nextLine().trim().toLowerCase();
            if (answer.equals("y")) {
                return true;
            }
            if (answer.equals("n")) {
                return false;
            }
            System.out.print("Please enter y or n: ");
        }
    }
    public static LocalDate readDate(Scanner in) {
        while (true) {
            System.out.print("Due date (MM-DD-YYYY): ");
            try {
                LocalDate date = LocalDate.parse(in.nextLine().trim(), DATE_FORMAT);
                if (!date.isBefore(LocalDate.now())) {
                    return date;
                }
                System.out.println("That date has already passed");
                System.out.print("Do you want to keep it anyway? (y/n): ");
                if (readYesNo(in)) {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Enter a real date as MM-DD-YYYY");
            }
        }
    }
    public static LocalTime readTime(Scanner in) {
        while (true) {
            System.out.print("Due time (HH:MM): ");
            try {
                return LocalTime.parse(in.nextLine().trim(), TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Enter a time as HH:MM (00:00 to 23:59)");
            }
        }
    }
    public static String readStatus(Scanner in) {
        while (true) {
            System.out.print("Status (not started/in-progress/complete): ");
            String answer = in.nextLine().trim().toLowerCase();
            if (answer.equals("not started") || answer.equals("in-progress") || answer.equals("complete")) {
                return answer;
            }
            System.out.println("Your options are: not started, in-progress, complete");
        }
    }
 
    public static String readPriority(Scanner in) {
        while (true) {
            System.out.print("Priority (low/medium/high): ");
            String answer = in.nextLine().trim().toLowerCase();
            if (answer.equals("low") || answer.equals("medium") || answer.equals("high")) {
                return answer;
            }
            System.out.println("Your options are: low, medium, high");
        }
    }
    public static int readMenuChoice(Scanner in, int first, int last) {
        while (true) {
            System.out.print("Choice (" + first + "-" + last + "): ");
            try {
                int choice = Integer.parseInt(in.nextLine().trim());
                if (choice >= first && choice <= last) {
                    return choice;
                }
            } catch (NumberFormatException e) { 

            }
            System.out.println("Enter a number from " + first + " to " + last);
        }
    }
}