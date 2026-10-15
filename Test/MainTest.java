package Test;

public import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Tests for Main. main() reads from System.in and saves to tasks.txt, so
 * these tests swap in fake typing and put tasks.txt back the way it was
 * when they're done.
 *
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class MainTest {

    private static final Path SAVE_FILE = Paths.get("tasks.txt");

    private PrintStream originalOut;
    private InputStream originalIn;
    private ByteArrayOutputStream output;
    private byte[] savedTasks; // null if there was no tasks.txt

    /*
    Backs up tasks.txt and captures System.out.
    @throws IOException if tasks.txt can't be read
    */

    @BeforeEach
    public void setUp() throws IOException {
        if (Files.exists(SAVE_FILE)) {
            this.savedTasks = Files.readAllBytes(SAVE_FILE);
        }
        else {
            this.savedTasks = null;
        }
        this.originalOut = System.out;
        this.originalIn = System.in;
        this.output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.output));
    }

    /*
    Puts System.out, System.in, and tasks.txt back.
    @throws IOException if tasks.txt can't be restored
    */

    @AfterEach
    public void tearDown() throws IOException {
        System.setOut(this.originalOut);
        System.setIn(this.originalIn);
        if (this.savedTasks != null) {
            Files.write(SAVE_FILE, this.savedTasks);
        }
        else {
            Files.deleteIfExists(SAVE_FILE);
        }
    }

    /*
    Pretends the user typed the given lines.
    @param typed what the user types, one answer per line
    */

    private void type(String typed) {
        System.setIn(new ByteArrayInputStream(typed.getBytes()));
    }

    /*
    Choosing 6 right away quits.
    */

    @Test
    public void testQuit() {
        this.type("6\n");
        Main.main(new String[0]);
        assertTrue(this.output.toString().contains("See you next time!"));
    }

    /*
    A bad menu choice is re-prompted, then 6 still quits.
    */

    @Test
    public void testBadMenuChoice() {
        this.type("abc\n6\n");
        Main.main(new String[0]);
        String text = this.output.toString();
        assertTrue(text.contains("Enter a number from 1 to 6"));
        assertTrue(text.contains("See you next time!"));
    }

} {
    
}
