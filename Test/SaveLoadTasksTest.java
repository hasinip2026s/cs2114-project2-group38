import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Tests for SaveLoadTasks. Uses its own test file so it never touches
 * the real tasks.txt.
 *
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class SaveLoadTasksTest {

    private static final String TEST_FILE = "test_tasks.txt";
    private static final String TEST_FOLDER = "test_folder";

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
    Puts System.out back and deletes anything the test made.
    */

    @AfterEach
    public void tearDown() {
        System.setOut(this.originalOut);
        new File(TEST_FILE).delete();
        new File(TEST_FOLDER).delete();
    }

    /*
    Saving 3 tasks and loading them gives the same 3 tasks in the same
    order. One name has a comma and spaces to make sure it still works.
    */

    @Test
    public void testSaveThenLoad() {
        LinkedChain<Task> tasks = new LinkedChain<Task>();
        tasks.addToEnd(new Task("Lab, part 2",
            LocalDateTime.of(2030, 12, 1, 17, 0), "not started", "low"));
        tasks.addToEnd(new Task("Laundry",
            LocalDateTime.of(2030, 12, 2, 9, 0), "in-progress", "high"));
        tasks.addToEnd(new Task("Essay",
            LocalDateTime.of(2030, 12, 3, 23, 59), "complete", "medium"));

        new SaveLoadTasks(TEST_FILE).save(tasks);
        LinkedChain<Task> loaded = new SaveLoadTasks(TEST_FILE).load();

        assertEquals(3, loaded.size());
        assertEquals(tasks.get(0), loaded.get(0));
        assertEquals(tasks.get(1), loaded.get(1));
        assertEquals(tasks.get(2), loaded.get(2));
    }

    /*
    If the file can't be written (here the path is a folder), save prints
    "Could not save tasks." instead of crashing.
    */

    @Test
    public void testSaveCantWrite() {
        new File(TEST_FOLDER).mkdir();
        new SaveLoadTasks(TEST_FOLDER).save(new LinkedChain<Task>());
        assertTrue(this.output.toString().contains("Could not save tasks."));
    }

    /*
    A missing file gives an empty chain with no error.
    */

    @Test
    public void testLoadMissingFile() {
        LinkedChain<Task> loaded =
            new SaveLoadTasks("no_such_file.txt").load();
        assertTrue(loaded.isEmpty());
        assertFalse(this.output.toString().contains("Skipped"));
    }

    /*
    A file with one bad line out of 3 loads the 2 good tasks and says
    one was skipped.
    @throws IOException if the test file can't be written
    */

    @Test
    public void testLoadSkipsBadLine() throws IOException {
        PrintWriter writer = new PrintWriter(TEST_FILE);
        writer.println("Laundry|2030-12-01T17:00|not started|low");
        writer.println("broken line");
        writer.println("Essay|2030-12-02T09:00|in-progress|high");
        writer.close();

        LinkedChain<Task> loaded = new SaveLoadTasks(TEST_FILE).load();

        assertEquals(2, loaded.size());
        assertEquals("Laundry", loaded.get(0).getName());
        assertEquals("Essay", loaded.get(1).getName());
        assertTrue(this.output.toString()
            .contains("Skipped 1 unreadable task(s)."));
    }

}