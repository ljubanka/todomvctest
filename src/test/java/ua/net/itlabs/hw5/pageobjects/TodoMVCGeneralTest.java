package ua.net.itlabs.hw5.pageobjects;

import org.junit.Test;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.COMPLETED;

public class TodoMVCGeneralTest extends BaseTest {
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

}
