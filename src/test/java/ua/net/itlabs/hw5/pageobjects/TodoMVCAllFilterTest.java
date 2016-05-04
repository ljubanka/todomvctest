package ua.net.itlabs.hw5.pageobjects;

import org.junit.Test;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.COMPLETED;

public class TodoMVCAllFilterTest {
    @Test
    public void testComplete() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1", "2");

        page.toggle("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopen() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(COMPLETED, "1", "2");

        page.toggle("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(1);
    }

    @Test
    public void testCompleteAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED), page.aTask("3", ACTIVE));

        page.toggleAll();
        page.assertTasks("1", "2", "3");
        page.assertItemsLeft(0);
    }

    @Test
    public void testClearCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(COMPLETED, "1", "2", "3");

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void testCancelEdit() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1");

        page.startEdit("1", "1 cancel edit").pressEscape();
        page.assertTasks("1");
        page.assertItemsLeft(1);
    }

    @Test
    public void testEditClickTab() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1", "2", "3");

        page.startEdit("2", "2 edited").pressTab();
        page.assertTasks("1", "2 edited", "3");
        page.assertItemsLeft(3);
    }

    @Test
    public void testDeleteByEmpty() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1", "2", "3");

        page.startEdit("2", "").pressEnter();
        page.assertTasks("1", "3");
    }
}
