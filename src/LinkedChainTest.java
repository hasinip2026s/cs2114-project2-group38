import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedChainTest {

    private LinkedChain<String> chain;

    /*
    Starts each test with an empty chain.
    */

    @BeforeEach
    public void setUp() {
        this.chain = new LinkedChain<String>();
    }

    /*
    A new chain is empty.
    */

    @Test
    public void testConstructor() {
        assertEquals(0, this.chain.size());
        assertTrue(this.chain.isEmpty());
        assertEquals("[]", this.chain.toString());
    }

    /*
    add() puts new entries at the front.
    */

    @Test
    public void testAdd() {
        this.chain.add("A");
        this.chain.add("B");
        assertEquals("[B, A]", this.chain.toString());
        assertEquals(2, this.chain.size());
    }

    /*
    add(null) throws and leaves the chain unchanged.
    */

    @Test
    public void testAddNull() {
        assertThrows(IllegalArgumentException.class,
            () -> this.chain.add(null));
        assertEquals(0, this.chain.size());
    }

    /*
    addToEnd() keeps the order things were added in.
    */

    @Test
    public void testAddToEnd() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        assertEquals("[A, B]", this.chain.toString());
        assertEquals("A", this.chain.get(0));
        assertEquals("B", this.chain.get(1));
    }

    /*
    addToEnd(null) throws and leaves the chain unchanged.
    */

    @Test
    public void testAddToEndNull() {
        assertThrows(IllegalArgumentException.class,
            () -> this.chain.addToEnd(null));
        assertEquals(0, this.chain.size());
    }

    /*
    add() on an empty chain also sets the tail, so addToEnd() after it
    goes in the right place.
    */

    @Test
    public void testAddThenAddToEnd() {
        this.chain.add("A");
        this.chain.addToEnd("B");
        assertEquals("[A, B]", this.chain.toString());
    }

    /*
    remove() takes off and returns the first entry.
    */

    @Test
    public void testRemoveFirst() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        assertEquals("A", this.chain.remove());
        assertEquals("[B]", this.chain.toString());
    }

    /*
    remove() on an empty chain returns null.
    */

    @Test
    public void testRemoveFirstEmpty() {
        assertNull(this.chain.remove());
    }

    /*
    Removing the only entry resets the tail, so addToEnd() still works.
    */

    @Test
    public void testRemoveOnlyThenAddToEnd() {
        this.chain.addToEnd("A");
        this.chain.remove();
        this.chain.addToEnd("B");
        assertEquals("[B]", this.chain.toString());
        assertEquals(1, this.chain.size());
    }

    /*
    remove(T) removes only the first match.
    */

    @Test
    public void testRemoveEntry() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        this.chain.addToEnd("A");
        assertTrue(this.chain.remove("A"));
        assertEquals("[B, A]", this.chain.toString());
    }

    /*
    Removing something that isn't there (or null) returns false and
    changes nothing.
    */

    @Test
    public void testRemoveEntryMissing() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        assertFalse(this.chain.remove("C"));
        assertFalse(this.chain.remove(null));
        assertEquals("[A, B]", this.chain.toString());
    }

    /*
    Removing the last entry moves the tail back one node, so addToEnd()
    after it goes in the right place.
    */

    @Test
    public void testRemoveLastThenAddToEnd() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        this.chain.addToEnd("C");
        this.chain.remove("C");
        this.chain.addToEnd("D");
        assertEquals("[A, B, D]", this.chain.toString());
    }

    /*
    clear() empties the chain, and addToEnd() still works after.
    */

    @Test
    public void testClear() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        this.chain.clear();
        assertTrue(this.chain.isEmpty());
        this.chain.addToEnd("C");
        assertEquals("[C]", this.chain.toString());
    }

    /*
    get() returns the entry at that position.
    */

    @Test
    public void testGet() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        this.chain.addToEnd("C");
        assertEquals("B", this.chain.get(1));
    }

    /*
    get() with a bad index throws.
    */

    @Test
    public void testGetBadIndex() {
        this.chain.addToEnd("A");
        assertThrows(IndexOutOfBoundsException.class,
            () -> this.chain.get(-1));
        assertThrows(IndexOutOfBoundsException.class,
            () -> this.chain.get(1));
    }

    /*
    contains() finds an entry that's there.
    */

    @Test
    public void testContains() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        assertTrue(this.chain.contains("B"));
    }

    /*
    contains() is false for something missing or on an empty chain.
    */

    @Test
    public void testContainsMissing() {
        assertFalse(this.chain.contains("A"));
        this.chain.addToEnd("A");
        assertFalse(this.chain.contains("C"));
    }

    /*
    count() counts every match.
    */

    @Test
    public void testCount() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        this.chain.addToEnd("A");
        assertEquals(2, this.chain.count("A"));
    }

    /*
    count() is 0 for something missing or for null.
    */

    @Test
    public void testCountMissing() {
        this.chain.addToEnd("A");
        assertEquals(0, this.chain.count("C"));
        assertEquals(0, this.chain.count(null));
    }

    /*
    size() and isEmpty() update after adds and removes.
    */

    @Test
    public void testSizeAndIsEmpty() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        this.chain.addToEnd("C");
        this.chain.remove("B");
        assertEquals(2, this.chain.size());
        assertFalse(this.chain.isEmpty());
    }

    /*
    toArray() copies the entries in order.
    */

    @Test
    public void testToArray() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        String[] result = this.chain.toArray(new String[2]);
        assertArrayEquals(new String[] {"A", "B"}, result);
    }

    /*
    toArray() throws for a null or too-small array.
    */

    @Test
    public void testToArrayBad() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        assertThrows(IllegalArgumentException.class,
            () -> this.chain.toArray(null));
        assertThrows(IllegalArgumentException.class,
            () -> this.chain.toArray(new String[1]));
    }

    /*
    A for-each loop visits every entry in order.
    */

    @Test
    public void testIterator() {
        this.chain.addToEnd("A");
        this.chain.addToEnd("B");
        this.chain.addToEnd("C");
        String visited = "";
        for (String entry : this.chain) {
            visited = visited + entry;
        }
        assertEquals("ABC", visited);
    }

    /*
    On an empty chain the loop runs zero times, and next() past the end
    throws.
    */

    @Test
    public void testIteratorEmpty() {
        Iterator<String> it = this.chain.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, () -> it.next());
    }

}