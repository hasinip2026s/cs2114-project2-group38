
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
* @author Cassidy McCahill
* @version 2026.10.15
*/

public class ViewListTest {

    private String capture(LinkedChain<Task> tasks) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            ViewList.show(tasks);
        } finally {
            System.setOut(original);
        }
        return out.toString();
    }

    @Test
    void emptyListPrintsMessage() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        String output = capture(tasks);
        assertTrue(output.contains("There are no tasks in your list yet."));
    }
   
    @Test
    void tasksArePrintedInOrderWithNumbers() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        Task first = new Task("Buy groceries", LocalDateTime.of(2026, 10, 20, 9, 0), "not started", "low");
        Task second = new Task("Finish project", LocalDateTime.of(2026, 10, 22, 17, 0), "in-progress", "high");
        tasks.add(first);
        tasks.add(second);

        String output = capture(tasks);

        assertTrue(output.contains("1. " + first));
        assertTrue(output.contains("2. " + second));
        assertTrue(output.indexOf("1. " + first) < output.indexOf("2. " + second));
    }

    @Test
    void nullChainThrows() {
        try {
            ViewList.show(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {

        }
    }
}

