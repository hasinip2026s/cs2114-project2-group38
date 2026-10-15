package Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import InputValidator;

/*
 * Tests for InputValidator. Each Scanner is built from a string, with one
 * typed answer per line, so the tests don't need a real keyboard.
 *
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class InputValidatorTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    /*
    Sends System.out into output so we can check what was printed.
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
    Counts how many times a message was printed.
    @param message the message to look for
    @return how many times it shows up in the output
    */

    private int timesPrinted(String message) {
        String text = this.output.toString();
        int count = 0;
        int index = text.indexOf(message);
        while (index != -1) {
            count++;
            index = text.indexOf(message, index + 1);
        }
        return count;
    }

    /*
    A normal name comes back as typed.
    */

    @Test
    public void testReadName() {
        Scanner in = new Scanner("Buy textbook\n");
        assertEquals("Buy textbook", InputValidator.readName(in));
    }

    /*
    A blank name is rejected once, then the next name is accepted.
    */

    @Test
    public void testReadNameEmpty() {
        Scanner in = new Scanner("\nBuy textbook\n");
        assertEquals("Buy textbook", InputValidator.readName(in));
        assertEquals(1, this.timesPrinted("can't be empty"));
    }

    /*
    A name with | is rejected so the save file stays readable.
    */

    @Test
    public void testReadNameWithPipe() {
        Scanner in = new Scanner("Lab | part 2\nLab part 2\n");
        assertEquals("Lab part 2", InputValidator.readName(in));
        assertEquals(1, this.timesPrinted("can't contain |"));
    }

    /*
    A real future date is returned.
    */

    @Test
    public void testReadDate() {
        Scanner in = new Scanner("10-02-2030\n");
        assertEquals(LocalDate.of(2030, 10, 2),
            InputValidator.readDate(in));
    }

    /*
    An impossible date is rejected once, then the next date is accepted.
    */

    @Test
    public void testReadDateImpossible() {
        Scanner in = new Scanner("02-30-2030\n03-01-2030\n");
        assertEquals(LocalDate.of(2030, 3, 1), InputValidator.readDate(in));
        assertEquals(1,
            this.timesPrinted("Enter a real date as MM-DD-YYYY"));
    }

    /*
    A past date warns, and "y" keeps it.
    */

    @Test
    public void testReadDatePastKeep() {
        Scanner in = new Scanner("01-15-2020\ny\n");
        assertEquals(LocalDate.of(2020, 1, 15),
            InputValidator.readDate(in));
        assertEquals(1, this.timesPrinted("That date has already passed"));
    }

    /*
    A past date warns, and "n" asks for a new date.
    */

    @Test
    public void testReadDatePastReenter() {
        Scanner in = new Scanner("01-15-2020\nn\n12-01-2030\n");
        assertEquals(LocalDate.of(2030, 12, 1),
            InputValidator.readDate(in));
        assertEquals(1, this.timesPrinted("That date has already passed"));
    }

    /*
    A real time is returned.
    */

    @Test
    public void testReadTime() {
        Scanner in = new Scanner("17:00\n");
        assertEquals(LocalTime.of(17, 0), InputValidator.readTime(in));
    }

    /*
    A bad time is rejected once, then the next time is accepted.
    */

    @Test
    public void testReadTimeBad() {
        Scanner in = new Scanner("25:70\n09:30\n");
        assertEquals(LocalTime.of(9, 30), InputValidator.readTime(in));
        assertEquals(1,
            this.timesPrinted("Enter a time as HH:MM (00:00 to 23:59)"));
    }

    /*
    Capital letters are fine and come back lowercase.
    */

    @Test
    public void testReadStatus() {
        Scanner in = new Scanner("In-Progress\n");
        assertEquals("in-progress", InputValidator.readStatus(in));
    }

    /*
    A status that isn't an option shows the options and asks again.
    */

    @Test
    public void testReadStatusBad() {
        Scanner in = new Scanner("urgent\nnot started\n");
        assertEquals("not started", InputValidator.readStatus(in));
        assertEquals(1, this.timesPrinted("Your options are"));
    }

    /*
    Capital letters are fine and come back lowercase.
    */

    @Test
    public void testReadPriority() {
        Scanner in = new Scanner("HIGH\n");
        assertEquals("high", InputValidator.readPriority(in));
    }

    /*
    A priority that isn't an option shows the options and asks again.
    */

    @Test
    public void testReadPriorityBad() {
        Scanner in = new Scanner("urgent\nhigh\n");
        assertEquals("high", InputValidator.readPriority(in));
        assertEquals(1, this.timesPrinted("Your options are"));
    }

    /*
    A number in range is returned.
    */

    @Test
    public void testReadMenuChoice() {
        Scanner in = new Scanner("3\n");
        assertEquals(3, InputValidator.readMenuChoice(in, 1, 6));
    }

    /*
    Letters and out-of-range numbers are both rejected.
    */

    @Test
    public void testReadMenuChoiceBad() {
        Scanner in = new Scanner("abc\n9\n2\n");
        assertEquals(2, InputValidator.readMenuChoice(in, 1, 6));
        assertEquals(2, this.timesPrinted("Enter a number from 1 to 6"));
    }

    /*
    "Y" (capital) counts as yes.
    */

    @Test
    public void testReadYesNo() {
        Scanner in = new Scanner("Y\n");
        assertTrue(InputValidator.readYesNo(in));
    }

    /*
    Anything other than y or n asks again.
    */

    @Test
    public void testReadYesNoBad() {
        Scanner in = new Scanner("maybe\nn\n");
        assertFalse(InputValidator.readYesNo(in));
        assertEquals(1, this.timesPrinted("Please enter y or n"));
    }

}