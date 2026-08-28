package prism.tasklist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import prism.PrismException;
import prism.task.Task;
import prism.task.Todo;

public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        this.taskList = new TaskList();
    }

    @Test
    public void addTask_singleTask_sizeIncreasesAndTaskReturned() {
        Task todo = new Todo("read book");
        Task added = this.taskList.addTask(todo);

        assertEquals(1, this.taskList.getSize());
        assertEquals(todo, added);
    }

    @Test
    public void deleteTask_validIndex_taskRemovedAndReturned() throws PrismException {
        Task todo1 = new Todo("read book");
        Task todo2 = new Todo("buy groceries");
        this.taskList.addTask(todo1);
        this.taskList.addTask(todo2);

        Task removed = this.taskList.deleteTask(0);

        assertEquals(1, this.taskList.getSize());
        assertEquals(todo1, removed);
        assertEquals(todo2, this.taskList.getTask(0));
    }

    @Test
    public void deleteTask_negativeIndex_throwsException() {
        this.taskList.addTask(new Todo("read book"));
        assertThrows(PrismException.class, () -> this.taskList.deleteTask(-1));
    }

    @Test
    public void deleteTask_outOfBoundsIndex_throwsException() {
        this.taskList.addTask(new Todo("read book"));
        assertThrows(PrismException.class, () -> this.taskList.deleteTask(5));
    }
}