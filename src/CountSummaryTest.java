import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/*
 * Tests for CountSummary.
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class CountSummaryTest {

    /*
    Counts each status and the high-priority tasks.
    */

    @Test
    public void testSummarize() {
        LocalDateTime due = LocalDateTime.of(2030, 12, 1, 17, 0);
        LinkedChain<Task> tasks = new LinkedChain<Task>();
        tasks.addToEnd(new Task("A", due, "not started", "low"));
        tasks.addToEnd(new Task("B", due, "in-progress", "high"));
        tasks.addToEnd(new Task("C", due, "complete", "high"));
        tasks.addToEnd(new Task("D", due, "not started", "medium"));

        assertEquals("4 tasks: 2 not started, 1 in-progress, "
            + "2 high priority.", CountSummary.summarize(tasks));
    }

    /*
    An empty chain gives all zeros without crashing.
    */

    @Test
    public void testSummarizeEmpty() {
        assertEquals("0 tasks: 0 not started, 0 in-progress, "
            + "0 high priority.",
            CountSummary.summarize(new LinkedChain<Task>()));
    }

    /*
    One task says "task", not "tasks".
    */

    @Test
    public void testSummarizeOneTask() {
        LinkedChain<Task> tasks = new LinkedChain<Task>();
        tasks.addToEnd(new Task("A", LocalDateTime.of(2030, 12, 1, 17, 0),
            "in-progress", "low"));
        assertEquals("1 task: 0 not started, 1 in-progress, "
            + "0 high priority.", CountSummary.summarize(tasks));
    }

}