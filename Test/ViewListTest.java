package Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import LinkedChain;
import Task;
import ViewList;

/*
 * Tests for ViewList.
 *
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class ViewListTest {

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
    Two tasks print as "1. ..." and "2. ..." in the order they were
    added.
    */

    @Test
    public void testShowTwoTasks() {
        LinkedChain<Task> tasks = new LinkedChain<Task>();
        tasks.addToEnd(new Task("Buy textbook",
            LocalDateTime.of(2030, 12, 1, 17, 0), "not started", "low"));
        tasks.addToEnd(new Task("Laundry",
            LocalDateTime.of(2030, 12, 2, 9, 0), "not started", "high"));

        ViewList.show(tasks);

        String text = this.output.toString();
        assertTrue(text.contains("1. Buy textbook"));
        assertTrue(text.contains("2. Laundry"));
    }

    /*
    An empty chain prints the empty-list message.
    */

    @Test
    public void testShowEmpty() {
        ViewList.show(new LinkedChain<Task>());
        assertTrue(this.output.toString()
            .contains("There are no tasks in your list yet."));
    }

}