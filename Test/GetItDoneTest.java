package Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import GetItDone;
import LinkedChain;
import Task;

/*
 * Tests for GetItDone.
 *
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class GetItDoneTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream output;
    private LinkedChain<Task> tasks;

    /*
    Starts each test with an empty chain and captures System.out.
    */

    @BeforeEach
    public void setUp() {
        this.originalOut = System.out;
        this.output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.output));
        this.tasks = new LinkedChain<Task>();
    }

    /*
    Puts System.out back.
    */

    @AfterEach
    public void tearDown() {
        System.setOut(this.originalOut);
    }

    /*
    The task with the earlier date wins.
    */

    @Test
    public void testPickEarliestDate() {
        this.tasks.addToEnd(new Task("Essay",
            LocalDateTime.of(2030, 10, 5, 9, 0), "not started", "low"));
        this.tasks.addToEnd(new Task("Quiz",
            LocalDateTime.of(2030, 10, 3, 9, 0), "not started", "low"));
        assertEquals("Quiz", GetItDone.pick(this.tasks).getName());
    }

    /*
    On the same date, higher priority wins, even if its time is later.
    */

    @Test
    public void testPickHigherPriority() {
        this.tasks.addToEnd(new Task("Laundry",
            LocalDateTime.of(2030, 10, 3, 9, 0), "not started", "low"));
        this.tasks.addToEnd(new Task("Lab",
            LocalDateTime.of(2030, 10, 3, 17, 0), "not started", "high"));
        assertEquals("Lab", GetItDone.pick(this.tasks).getName());
    }

    /*
    An empty chain gives null.
    */

    @Test
    public void testPickEmpty() {
        assertNull(GetItDone.pick(this.tasks));
    }

    /*
    If every task is complete, there's nothing to pick.
    */

    @Test
    public void testPickAllComplete() {
        this.tasks.addToEnd(new Task("Essay",
            LocalDateTime.of(2030, 10, 5, 9, 0), "complete", "high"));
        assertNull(GetItDone.pick(this.tasks));
    }

    /*
    show prints the picked task.
    */

    @Test
    public void testShow() {
        Task lab = new Task("Lab 3", LocalDateTime.of(2030, 10, 3, 9, 0),
            "not started", "medium");
        this.tasks.addToEnd(lab);
        GetItDone.show(this.tasks);
        assertTrue(this.output.toString().contains(lab.toString()));
    }

    /*
    show on an empty chain prints "Nothing to do!" without crashing.
    */

    @Test
    public void testShowEmpty() {
        GetItDone.show(this.tasks);
        assertTrue(this.output.toString().contains("Nothing to do!"));
    }

}