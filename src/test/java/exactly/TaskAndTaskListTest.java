package exactly;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class TaskAndTaskListTest {

    @Test
    void testEqualsAndHashCode() {
        // Todo equality
        Todo t1 = new Todo("read book");
        Todo t2 = new Todo("read book");
        assertEquals(t1, t2, "Two Todos with same description should be equal");
        assertEquals(t1.hashCode(), t2.hashCode(),
                "Equal objects must have same hashCode");

        // Deadline equality
        Deadline d1 = new Deadline("submit", "2025-05-01");
        Deadline d2 = new Deadline("submit", "2025-05-01");
        assertEquals(d1, d2, "Two Deadlines with same desc+date should be equal");
        assertEquals(d1.hashCode(), d2.hashCode(),
                "Equal Deadlines must have same hashCode");

        // Inequality across types or data
        Event e = new Event("submit", "10:00", "11:00");
        assertNotEquals(d1, e, "Deadline and Event should not be equal");
        Todo t3 = new Todo("write code");
        assertNotEquals(t1, t3, "Different Todos should not be equal");
    }

    @Test
    void testTaskListAddRemove() {
        TaskList list = new TaskList();
        assertEquals(0, list.size(), "New TaskList should be empty");

        Todo todo = new Todo("groceries");
        list.add(todo);
        assertEquals(1, list.size(), "Size should be 1 after one add");
        assertSame(todo, list.get(0), "get(0) should return the added task");

        Task removed = list.remove(0);
        assertSame(todo, removed, "remove(0) should return the removed task");
        assertEquals(0, list.size(), "Size should be 0 after remove");
    }

    @Test
    void testListTasksFormatting() {
        TaskList emptyList = new TaskList();
        String emptyOutput = emptyList.listTasks();
        assertTrue(emptyOutput.contains("task list is empty"),
                "Empty list should mention it is empty");

        TaskList list = new TaskList();
        list.add(new Todo("A"), new Deadline("B", "2025-12-31"));
        String output = list.listTasks();
        // It should list two tasks numbered 1 and 2
        assertTrue(output.contains("1. [T][ ] A"), "First line should show Todo A");
        assertTrue(output.contains("2. [D][ ] B"), "Second line should show Deadline B");
    }
}
