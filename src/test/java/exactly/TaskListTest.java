package exactly;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void testEmptyTaskList() {
        TaskList taskList = new TaskList();
        String listOutput = taskList.listTasks();
        // Check that the empty message is returned
        assertTrue(listOutput.contains("your task list is empty"));
    }

    @Test
    public void testListTasksWithTasks() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("read book"));
        taskList.add(new Deadline("return book", "2019-12-02"));
        String listOutput = taskList.listTasks();
        // Should contain both tasks
        assertTrue(listOutput.contains("[T]"));
        assertTrue(listOutput.contains("[D]"));
        // Also, check that the size is correct
        assertEquals(2, taskList.size());
    }
}
