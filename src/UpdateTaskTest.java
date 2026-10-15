
public import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Tests for UpdateTask.
 *
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class UpdateTaskTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    /*
    Captures System.out.
    */

    @BeforeEach
    public void setUp() {
        this.originalOut = System.out;
        this.output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.output));
    }

    /*
    Puts System.out back.
    */

    @AfterEach
    public void tearDown() {
        System.setOut(this.originalOut);
    }

    /*
    Picking task 1, then status, then "in-progress" updates the status.
    */

    @Test
    public void testUpdateStatus() {
        LinkedChain<Task> tasks = new LinkedChain<Task>();
        tasks.addToEnd(new Task("Buy textbook",
            LocalDateTime.of(2030, 12, 1, 17, 0), "not started", "low"));

        Scanner in = new Scanner("1\n1\nin-progress\n");
        assertTrue(UpdateTask.update(tasks, in));
        assertEquals("in-progress", tasks.get(0).getStatus());
    }

    /*
    An empty chain prints "No tasks yet." and returns false.
    */

    @Test
    public void testUpdateEmpty() {
        LinkedChain<Task> tasks = new LinkedChain<Task>();
        assertFalse(UpdateTask.update(tasks, new Scanner("")));
        assertTrue(this.output.toString().contains("No tasks yet."));
    }

} {
    
}
