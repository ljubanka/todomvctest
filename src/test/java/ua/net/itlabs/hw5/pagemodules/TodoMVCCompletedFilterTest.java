package ua.net.itlabs.hw5.pagemodules;

import org.junit.Test;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.*;
import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.TaskType.COMPLETED;

public class TodoMVCCompletedFilterTest {
    @Test
    public void testDelete() {
        TodoMVCPage page = new TodoMVCPage();
        givenAtCompleted(COMPLETED, "1", "2");

        delete("2");
        assertTasks("1");
        assertItemsLeft(0);
    }

    @Test
    public void testReopen() {
        TodoMVCPage page = new TodoMVCPage();
        givenAtCompleted(COMPLETED, "1");

        toggle("1");
        assertNoTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testClearCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        givenAtCompleted(COMPLETED, "1");

        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testReopenAll() {
        TodoMVCPage page = new TodoMVCPage();
        givenAtCompleted(COMPLETED, "1", "2");

        toggleAll();
        assertNoTasks();
        assertItemsLeft(2);
    }
}
