
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
* @author Cassidy McCahill
* @version 2026.10.15
*/

public class GetItDoneTest {

    private String capture(LinkedChain<Task> tasks) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            GetItDone.show(tasks);
        } finally {
            System.setOut(original);
        }
        return out.toString();
    }

    @Test
    void pickChoosesEarliestDueDate() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        Task later = new Task("Later", LocalDateTime.of(2026, 10, 25, 9, 0), "not started", "high");
        Task sooner = new Task("Sooner", LocalDateTime.of(2026, 10, 20, 9, 0), "not started", "low");
        tasks.add(later);
        tasks.add(sooner);

        assertSame(sooner, GetItDone.pick(tasks));
    }

    @Test
    void pickNullChainThrows() {
        try {
            GetItDone.pick(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    void pickEmptyChainReturnsNull() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        assertNull(GetItDone.pick(tasks));
    }

    @Test
    void pickSkipsCompleteTasks() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        Task done = new Task("Done", LocalDateTime.of(2026, 10, 18, 9, 0), "complete", "high");
        Task open = new Task("Open", LocalDateTime.of(2026, 10, 25, 9, 0), "not started", "low");
        tasks.add(done);
        tasks.add(open);

        assertSame(open, GetItDone.pick(tasks));
    }

    @Test
    void pickBreaksDateTieWithPriority() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        Task low = new Task("Low", LocalDateTime.of(2026, 10, 20, 9, 0), "not started", "low");
        Task high = new Task("High", LocalDateTime.of(2026, 10, 20, 17, 0), "not started", "high");
        tasks.add(low);
        tasks.add(high);

        assertSame(high, GetItDone.pick(tasks));
    }

    @Test
    void showPrintsPickedTask() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        Task only = new Task("Only", LocalDateTime.of(2026, 10, 20, 9, 0), "not started", "medium");
        tasks.add(only);

        String output = capture(tasks);

        assertTrue(output.contains(only.toString()));
    }

    @Test
    void showNullChainThrows() {
        try {
            GetItDone.show(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    void showEmptyChainPrintsNothingToDo() {
        LinkedChain<Task> tasks = new LinkedChain<>();
        String output = capture(tasks);
        assertTrue(output.contains("Nothing to do!"));
    }
}