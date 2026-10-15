
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class RemoveTaskTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream output;
    private LinkedChain<Task> tasks;

    /*
    Starts each test with two tasks and captures System.out.
    */

    @BeforeEach
    public void setUp() {
        this.originalOut = System.out;
        this.output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.output));

        this.tasks = new LinkedChain<Task>();
        this.tasks.addToEnd(new Task("Buy textbook",
            LocalDateTime.of(2030, 12, 1, 17, 0), "not started", "low"));
        this.tasks.addToEnd(new Task("Laundry",
            LocalDateTime.of(2030, 12, 2, 9, 0), "not started", "high"));
    }

    /*
    Puts System.out back.
    */

    @AfterEach
    public void tearDown() {
        System.setOut(this.originalOut);
    }

    /*
    Typing a task's name removes that task and leaves the other one.
    */

    @Test
    public void testRemove() {
        assertTrue(RemoveTask.remove(this.tasks,
            new Scanner("Buy textbook\n")));
        assertEquals(1, this.tasks.size());
        assertEquals("Laundry", this.tasks.get(0).getName());
    }

    /*
    A name that isn't in the list prints a message and changes nothing.
    */

    @Test
    public void testRemoveMissing() {
        assertFalse(RemoveTask.remove(this.tasks, new Scanner("Gym\n")));
        assertEquals(2, this.tasks.size());
        assertTrue(this.output.toString()
            .contains("No task named Gym found"));
    }

    /*
    An empty chain prints "No tasks yet." and returns false.
    */

    @Test
    public void testRemoveEmpty() {
        LinkedChain<Task> empty = new LinkedChain<Task>();
        assertFalse(RemoveTask.remove(empty, new Scanner("")));
        assertTrue(this.output.toString().contains("No tasks yet."));
    }

}