package Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
* @author Cassidy McCahill
* @version 2026.10.15
*/

public class InputValidatorTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    // Capture printed output so tests can check error messages
    @BeforeEach
    void captureOutput() {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    private Scanner scan(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    private String printed() {
        return out.toString();
    }

    private String futureDate() {
        return LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("MM-dd-uuuu"));
    }

    // ---------- readName ----------

    // normal case
    @Test
    void readNameReturnsValidName() {
        assertEquals("Buy groceries", InputValidator.readName(scan("Buy groceries\n")));
    }

    @Test
    void readNameTrimsWhitespace() {
        assertEquals("Buy groceries", InputValidator.readName(scan("   Buy groceries   \n")));
    }

    // bad-input cases
    @Test
    void readNameRejectsEmptyName() {
        String result = InputValidator.readName(scan("\nBuy groceries\n"));
        assertEquals("Buy groceries", result);
        assertTrue(printed().contains("The task name can't be empty"));
    }

    @Test
    void readNameRejectsPipeCharacter() {
        String result = InputValidator.readName(scan("Buy|groceries\nBuy groceries\n"));
        assertEquals("Buy groceries", result);
        assertTrue(printed().contains("The task name can't contain |"));
    }

    // ---------- readYesNo ----------

    // normal cases
    @Test
    void readYesNoAcceptsY() {
        assertTrue(InputValidator.readYesNo(scan("y\n")));
    }

    @Test
    void readYesNoAcceptsUppercaseN() {
        assertFalse(InputValidator.readYesNo(scan("N\n")));
    }

    // bad-input case
    @Test
    void readYesNoRejectsOtherAnswers() {
        boolean result = InputValidator.readYesNo(scan("maybe\ny\n"));
        assertTrue(result);
        assertTrue(printed().contains("Please enter y or n"));
    }

    // ---------- readDate ----------

    // normal case
    @Test
    void readDateAcceptsFutureDate() {
        LocalDate expected = LocalDate.now().plusDays(10);
        assertEquals(expected, InputValidator.readDate(scan(futureDate() + "\n")));
    }

    // bad-input cases
    @Test
    void readDateRejectsNonsense() {
        LocalDate result = InputValidator.readDate(scan("abc\n" + futureDate() + "\n"));
        assertEquals(LocalDate.now().plusDays(10), result);
        assertTrue(printed().contains("Enter a real date as MM-DD-YYYY"));
    }

    @Test
    void readDateRejectsImpossibleDate() {
        LocalDate result = InputValidator.readDate(scan("02-30-2030\n" + futureDate() + "\n"));
        assertEquals(LocalDate.now().plusDays(10), result);
        assertTrue(printed().contains("Enter a real date as MM-DD-YYYY"));
    }

    @Test
    void readDatePastDateDeclinedAsksAgain() {
        LocalDate result = InputValidator.readDate(scan("01-01-2020\nn\n" + futureDate() + "\n"));
        assertEquals(LocalDate.now().plusDays(10), result);
        assertTrue(printed().contains("That date has already passed"));
    }

    @Test
    void readDatePastDateKeptWhenConfirmed() {
        LocalDate result = InputValidator.readDate(scan("01-01-2020\ny\n"));
        assertEquals(LocalDate.of(2020, 1, 1), result);
    }

    // ---------- readTime ----------

    // normal cases
    @Test
    void readTimeAcceptsValidTime() {
        assertEquals(LocalTime.of(9, 30), InputValidator.readTime(scan("09:30\n")));
    }

    @Test
    void readTimeAcceptsLastMinuteOfDay() {
        assertEquals(LocalTime.of(23, 59), InputValidator.readTime(scan("23:59\n")));
    }

    // bad-input cases
    @Test
    void readTimeRejectsHourTooLarge() {
        LocalTime result = InputValidator.readTime(scan("25:00\n09:30\n"));
        assertEquals(LocalTime.of(9, 30), result);
        assertTrue(printed().contains("Enter a time as HH:MM"));
    }

    @Test
    void readTimeRejectsNonsense() {
        LocalTime result = InputValidator.readTime(scan("abc\n09:30\n"));
        assertEquals(LocalTime.of(9, 30), result);
        assertTrue(printed().contains("Enter a time as HH:MM"));
    }

    // ---------- readStatus ----------

    // normal cases
    @Test
    void readStatusAcceptsValidStatus() {
        assertEquals("complete", InputValidator.readStatus(scan("complete\n")));
    }

    @Test
    void readStatusIsCaseInsensitive() {
        assertEquals("in-progress", InputValidator.readStatus(scan("IN-PROGRESS\n")));
    }

    // bad-input case
    @Test
    void readStatusRejectsUnknownStatus() {
        String result = InputValidator.readStatus(scan("done\nnot started\n"));
        assertEquals("not started", result);
        assertTrue(printed().contains("Your options are: not started, in-progress, complete"));
    }

    // ---------- readPriority ----------

    // normal cases
    @Test
    void readPriorityAcceptsValidPriority() {
        assertEquals("high", InputValidator.readPriority(scan("high\n")));
    }

    @Test
    void readPriorityIsCaseInsensitive() {
        assertEquals("medium", InputValidator.readPriority(scan("Medium\n")));
    }

    // bad-input case
    @Test
    void readPriorityRejectsUnknownPriority() {
        String result = InputValidator.readPriority(scan("urgent\nlow\n"));
        assertEquals("low", result);
        assertTrue(printed().contains("Your options are: low, medium, high"));
    }

    // ---------- readMenuChoice ----------

    // normal cases
    @Test
    void readMenuChoiceAcceptsNumberInRange() {
        assertEquals(3, InputValidator.readMenuChoice(scan("3\n"), 1, 6));
    }

    @Test
    void readMenuChoiceAcceptsBothEnds() {
        assertEquals(1, InputValidator.readMenuChoice(scan("1\n"), 1, 6));
        assertEquals(6, InputValidator.readMenuChoice(scan("6\n"), 1, 6));
    }

    // bad-input cases
    @Test
    void readMenuChoiceRejectsTooSmall() {
        int result = InputValidator.readMenuChoice(scan("0\n3\n"), 1, 6);
        assertEquals(3, result);
        assertTrue(printed().contains("Enter a number from 1 to 6"));
    }

    @Test
    void readMenuChoiceRejectsTooLarge() {
        int result = InputValidator.readMenuChoice(scan("7\n3\n"), 1, 6);
        assertEquals(3, result);
        assertTrue(printed().contains("Enter a number from 1 to 6"));
    }

    @Test
    void readMenuChoiceRejectsText() {
        int result = InputValidator.readMenuChoice(scan("abc\n3\n"), 1, 6);
        assertEquals(3, result);
        assertTrue(printed().contains("Enter a number from 1 to 6"));
    }
}