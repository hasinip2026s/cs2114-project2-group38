import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class UpdateTask {

    /*
    * @author Hasini Pottipati
    * @version 2026.10.14
    */
 
    public static boolean update(LinkedChain<Task> tasks, Scanner in) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet.");
            return false;
        }
 
        ViewList.show(tasks);
        System.out.println("Which task do you want to update?");
        int choice = InputValidator.readMenuChoice(in, 1, tasks.size());
        Task task = tasks.get(choice - 1);
 
        System.out.println("What do you want to change?");
        System.out.println("1. Status");
        System.out.println("2. Priority");
        System.out.println("3. Name");
        System.out.println("4. Due date and time");
        int field = InputValidator.readMenuChoice(in, 1, 4);
 
        if (field == 1) {
            task.setStatus(InputValidator.readStatus(in));
        }
        else if (field == 2) {
            task.setPriority(InputValidator.readPriority(in));
        }
        else if (field == 3) {
            task.setName(InputValidator.readName(in));
        }
        else {
            // date and time are read separately, so we join them here
            LocalDate date = InputValidator.readDate(in);
            LocalTime time = InputValidator.readTime(in);
            task.setDue(LocalDateTime.of(date, time));
        }
        return true;
    }
}
 