package ua.net.itlabs.hw5.pageobjects;

import org.junit.Test;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import static com.codeborne.selenide.Selenide.$;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.COMPLETED;

public class TodoMVCActiveFilterTest {
    @Test
    public void testAdd() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1");

        page.add("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(2);
    }

    @Test
    public void testEdit() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1", "2");

        page.startEdit("2", "2 edited").pressEnter();
        page.assertTasks("1", "2 edited");
        page.assertItemsLeft(2);
    }

    @Test
    public void testDelete() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1");

        page.delete("1");
        page.assertNoTasks();
    }

    @Test
    public void testCompleteAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1", "2");

        page.toggleAll();
        page.assertNoTasks();
        page.assertItemsLeft(0);
    }

    @Test
    public void testClearCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED), page.aTask("3", COMPLETED));

        page.clearCompleted();
        page.assertTasks("1");
        page.assertItemsLeft(1);
    }

    @Test
    public void testEditClickOutside() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1", "2");

        page.startEdit("2", "2 edited");
        $("#new-todo").click();
        page.assertTasks("1", "2 edited");
        page.assertItemsLeft(2);
    }
}
