package ua.net.itlabs.hw5.pageobjects;

import org.junit.Test;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.COMPLETED;

public class TodoMVCCompletedFilterTest {
    @Test
    public void testDelete() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1", "2");

        page.delete("2");
        page.assertTasks("1");
        page.assertItemsLeft(0);
    }

    @Test
    public void testReopen() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1");

        page.toggle("1");
        page.assertNoTasks();
        page.assertItemsLeft(1);
    }

    @Test
    public void testClearCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1");

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void testReopenAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1", "2");

        page.toggleAll();
        page.assertNoTasks();
        page.assertItemsLeft(2);
    }
}
