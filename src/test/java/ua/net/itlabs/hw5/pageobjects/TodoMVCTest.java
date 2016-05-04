package ua.net.itlabs.hw5.pageobjects;

import org.junit.Test;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import static com.codeborne.selenide.Selenide.*;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.COMPLETED;

public class TodoMVCTest extends BaseTest {
    @Test
    public void testTasksCommonFlow() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll();

        page.add("1");
        page.startEdit("1", "1 edited").pressEnter();
        page.assertTasks("1 edited");
        page.assertItemsLeft(1);

        page.filterActive();
        page.assertTasks("1 edited");

        //complete
        page.toggle("1 edited");
        page.assertNoTasks();

        page.filterCompleted();
        page.assertTasks("1 edited");

        page.filterAll();
        page.assertTasks("1 edited");

        page.delete("1 edited");
        page.assertNoTasks();
    }

    @Test
    public void testAddAtActive() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1");

        page.add("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(2);
    }

    @Test
    public void testEditAtActive() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1", "2");

        page.startEdit("2", "2 edited").pressEnter();
        page.assertTasks("1", "2 edited");
        page.assertItemsLeft(2);
    }

    @Test
    public void testDeleteAtActive() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1");

        page.delete("1");
        page.assertNoTasks();
    }

    @Test
    public void testDeleteAtCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1", "2");

        page.delete("2");
        page.assertTasks("1");
        page.assertItemsLeft(0);
    }

    @Test
    public void testCompleteAtAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1", "2");

        page.toggle("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAtAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(COMPLETED, "1", "2");

        page.toggle("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAtCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1");

        page.toggle("1");
        page.assertNoTasks();
        page.assertItemsLeft(1);
    }

    @Test
    public void testCompleteAllAtAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED), page.aTask("3", ACTIVE));

        page.toggleAll();
        page.assertTasks("1", "2", "3");
        page.assertItemsLeft(0);
    }

    @Test
    public void testCompleteAllAtActive() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1", "2");

        page.toggleAll();
        page.assertNoTasks();
        page.assertItemsLeft(0);
    }


    @Test
    public void testClearCompletedAtAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(COMPLETED, "1", "2", "3");

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void testClearCompletedAtActive() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED), page.aTask("3", COMPLETED));

        page.clearCompleted();
        page.assertTasks("1");
        page.assertItemsLeft(1);
    }

    @Test
    public void testClearCompletedAtCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1");

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void testCancelEditAtAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1");

        page.startEdit("1", "1 cancel edit").pressEscape();
        page.assertTasks("1");
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(COMPLETED, "1", "2");

        page.toggleAll();
        page.assertNoTasks();
        page.assertItemsLeft(2);

    }

    @Test
    public void testFilterFromAllToCompleted() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.filterCompleted();
        page.assertTasks("2");
        page.assertItemsLeft(1);
    }

    @Test
    public void testFilterFromActiveToAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.filterAll();
        page.assertTasks("1", "2");
        page.assertItemsLeft(1);
    }

    @Test
    public void testFilterFromCompletedToActive() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtCompleted(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.filterActive();
        page.assertTasks("1");
        page.assertItemsLeft(1);
    }

    @Test
    public void testEditClickOutsideAtActive() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtActive(ACTIVE, "1", "2");

        page.startEdit("2", "2 edited");
        $("#new-todo").click();
        page.assertTasks("1", "2 edited");
        page.assertItemsLeft(2);
    }

    @Test
    public void testEditClickTabAtAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1", "2", "3");

        page.startEdit("2", "2 edited").pressTab();
        page.assertTasks("1", "2 edited", "3");
        page.assertItemsLeft(3);
    }

    @Test
    public void testDeleteByEmptyAtAll() {
        TodoMVCPage page = new TodoMVCPage();
        page.givenAtAll(ACTIVE, "1", "2", "3");

        page.startEdit("2", "").pressEnter();
        page.assertTasks("1", "3");
    }


}
