import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
 
 /*
 * Collects a new task's fields and adds the task to the chain.
 * @author Hasini Pottipati
 * @version 2026.10.14
 */

public class AddTask {
 
    /*
    Reads the name, date, time, status, and priority through
    InputValidator, builds a Task, and adds it to the end of the chain.
    Works the same on an empty chain.
    Big-O: O(1), since addToEnd() links right after the tail.
    @param tasks the list of tasks
    @param in    the scanner to read from
    */
 
    public static void add(LinkedChain<Task> tasks, Scanner in) {
        String name = InputValidator.readName(in);
        LocalDate date = InputValidator.readDate(in);
        LocalTime time = InputValidator.readTime(in);
        String status = InputValidator.readStatus(in);
        String priority = InputValidator.readPriority(in);
 
        // date and time are read separately, so we join them here
        LocalDateTime due = LocalDateTime.of(date, time);
        tasks.addToEnd(new Task(name, due, status, priority));
    }
}