import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Tests for Task.
 *
 * Task doesn't check its own values. The spec says bad values are
 * caught in InputValidator before a Task is ever built, so the bad-input
 * cases for these values are in InputValidatorTest.
 */

public class TaskTest {

    private LocalDateTime due;
    private Task task;

    /*
    Makes a fresh task before every test.
    */

    @BeforeEach
    public void setUp() {
        this.due = LocalDateTime.of(2030, 12, 1, 17, 0);
        this.task = new Task("Buy textbook", this.due, "not started",
            "low");
    }

    /*
    The constructor stores all four values and the getters return them.
    */

    @Test
    public void testConstructorAndGetters() {
        assertEquals("Buy textbook", this.task.getName());
        assertEquals(this.due, this.task.getDue());
        assertEquals("not started", this.task.getStatus());
        assertEquals("low", this.task.getPriority());
    }

    /*
    setStatus changes only the status.
    */

    @Test
    public void testSetStatus() {
        this.task.setStatus("in-progress");
        assertEquals("in-progress", this.task.getStatus());
        assertEquals("low", this.task.getPriority());
    }

    /*
    setPriority changes only the priority.
    */

    @Test
    public void testSetPriority() {
        this.task.setPriority("high");
        assertEquals("high", this.task.getPriority());
        assertEquals("not started", this.task.getStatus());
    }

    /*
    setName changes only the name.
    */

    @Test
    public void testSetName() {
        this.task.setName("Return textbook");
        assertEquals("Return textbook", this.task.getName());
        assertEquals(this.due, this.task.getDue());
    }

    /*
    setDue changes only the due date and time.
    */

    @Test
    public void testSetDue() {
        LocalDateTime newDue = LocalDateTime.of(2030, 12, 5, 9, 30);
        this.task.setDue(newDue);
        assertEquals(newDue, this.task.getDue());
        assertEquals("Buy textbook", this.task.getName());
    }

    /*
    high is 3, medium is 2, low is 1.
    */

    @Test
    public void testGetPriorityRank() {
        assertEquals(1, this.task.getPriorityRank());
        this.task.setPriority("medium");
        assertEquals(2, this.task.getPriorityRank());
        this.task.setPriority("high");
        assertEquals(3, this.task.getPriorityRank());
    }

    /*
    Same values are equal, even if the name has different capitals.
    */

    @Test
    public void testEqualsSameTask() {
        Task same = new Task("buy TEXTBOOK", this.due, "not started",
            "low");
        assertTrue(this.task.equals(same));
    }

    /*
    A different due date, null, or something that isn't a Task is not
    equal.
    */

    @Test
    public void testEqualsDifferentTask() {
        Task later = new Task("Buy textbook",
            LocalDateTime.of(2030, 12, 2, 17, 0), "not started", "low");
        assertFalse(this.task.equals(later));
        assertFalse(this.task.equals(null));
        assertFalse(this.task.equals("Buy textbook"));
    }

    /*
    Tasks that are equal (even with different capitals in the name) have
    the same hash code.
    */

    @Test
    public void testHashCodeMatchesEquals() {
        Task same = new Task("BUY TEXTBOOK", this.due, "not started",
            "low");
        assertEquals(this.task.hashCode(), same.hashCode());
    }

    /*
    toString puts the task on one line.
    */

    @Test
    public void testToString() {
        assertEquals("Buy textbook — due 12-01-2030 17:00 — not started"
            + " — low", this.task.toString());
    }

    /*
    Single-digit months, days, hours, and minutes get a leading zero.
    */

    @Test
    public void testToStringPadsZeros() {
        this.task.setDue(LocalDateTime.of(2030, 1, 5, 9, 5));
        assertTrue(this.task.toString().contains("due 01-05-2030 09:05"));
    }

}