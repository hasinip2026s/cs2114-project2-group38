package Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import AddTask;
import LinkedChain;
import Task;

/*
 * Tests for AddTask.
 *
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class AddTaskTest {

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
    Valid answers add one task with those values.
    */

    @Test
    public void testAdd() {
        Scanner in = new Scanner(
            "Buy textbook\n12-01-2030\n17:00\nnot started\nlow\n");
        AddTask.add(this.tasks, in);

        Task expected = new Task("Buy textbook",
            LocalDateTime.of(2030, 12, 1, 17, 0), "not started", "low");
        assertEquals(1, this.tasks.size());
        assertEquals(expected, this.tasks.get(0));
    }

    /*
    A blank name is re-prompted, then the task is added normally.
    */

    @Test
    public void testAddBlankNameFirst() {
        Scanner in = new Scanner(
            "\nBuy textbook\n12-01-2030\n17:00\nnot started\nlow\n");
        AddTask.add(this.tasks, in);

        assertEquals(1, this.tasks.size());
        assertEquals("Buy textbook", this.tasks.get(0).getName());
        assertTrue(this.output.toString().contains("can't be empty"));
    }

}