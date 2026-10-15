
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

/**
* @author Cassidy McCahill
* @version 2026.10.15
*/

public class MainTest {

    // EDIT THESE to match the prompts in AddTask, UpdateTask, and RemoveTask.
    // Each is only the answers typed AFTER the menu number, one per line.
    private static final String ADD_ANSWERS = "Buy groceries\n2026-10-20 09:00\nnot started\nlow\n";
    private static final String ADDED_NAME = "Buy groceries";

    private static final String UPDATE_ANSWERS = "1\n";   // which task, plus whatever UpdateTask asks next
    private static final String UPDATED_TEXT = "complete"; // text that should appear in the list after the update

    private static final String CHECK_OFF_ANSWERS = "1\n"; // which task to check off

    @BeforeEach
    void deleteSaveFile() {
        new File("tasks.txt").delete();
    }

    @AfterEach
    void cleanUp() {
        new File("tasks.txt").delete();
    }

    // Feeds the given text to main() as keyboard input and returns what it printed
    private String run(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));
        try {
            Main.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return out.toString();
    }

    // ---------- Option 6: Quit ----------

    @Test
    void quitPrintsMenuAndGoodbye() {
        String output = run("6\n");
        assertTrue(output.contains("Check it off Menu:"));
        assertTrue(output.contains("See you next time!"));
    }

    // ---------- Bad menu input ----------

    @Test
    void invalidMenuChoiceIsRejected() {
        String output = run("9\nabc\n6\n");
        assertTrue(output.contains("See you next time!"));
    }

    // ---------- Option 1: Add a task ----------

    @Test
    void addedTaskAppearsInList() {
        String output = run("1\n" + ADD_ANSWERS + "2\n6\n");
        assertTrue(output.contains(ADDED_NAME));
    }

    // ---------- Option 2: View tasks ----------

    @Test
    void viewEmptyListShowsMessage() {
        String output = run("2\n6\n");
        assertTrue(output.contains("There are no tasks in your list yet."));
    }

    @Test
    void viewListShowsNumberedTask() {
        String output = run("1\n" + ADD_ANSWERS + "2\n6\n");
        assertTrue(output.contains("1. "));
    }

    // ---------- Option 3: Update a task ----------

    @Test
    void updatedTaskShowsNewValue() {
        String output = run("1\n" + ADD_ANSWERS + "3\n" + UPDATE_ANSWERS + "2\n6\n");
        assertTrue(output.contains(UPDATED_TEXT));
    }

    @Test
    void updateOnEmptyListStillQuits() {
        String output = run("3\n6\n");
        assertTrue(output.contains("See you next time!"));
    }

    // ---------- Option 4: Check off a task ----------

    @Test
    void checkedOffTaskIsGoneFromList() {
        String output = run("1\n" + ADD_ANSWERS + "4\n" + CHECK_OFF_ANSWERS + "2\n6\n");
        assertTrue(output.contains("There are no tasks in your list yet."));
    }

    @Test
    void checkOffOnEmptyListStillQuits() {
        String output = run("4\n6\n");
        assertTrue(output.contains("See you next time!"));
    }

    // ---------- Option 5: Get It Done ----------

    @Test
    void getItDoneOnEmptyListShowsNothingToDo() {
        String output = run("5\n6\n");
        assertTrue(output.contains("Nothing to do!"));
    }

    @Test
    void getItDonePicksAddedTask() {
        String output = run("1\n" + ADD_ANSWERS + "5\n6\n");
        assertTrue(output.contains(ADDED_NAME));
    }

    // ---------- Save and load ----------

    @Test
    void taskSurvivesQuitAndRestart() {
        run("1\n" + ADD_ANSWERS + "6\n");
        String output = run("2\n6\n");
        assertTrue(output.contains(ADDED_NAME));
    }
}