package ua.net.itlabs.hw5.pagemodules;

import org.junit.Test;

import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.*;
import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.TaskType.COMPLETED;

public class TodoMVCGeneralTest extends BaseTest {
    @Test
    public void testTasksCommonFlow() {
        givenAtAll();

        add("1");
        startEdit("1", "1 edited").pressEnter();
        assertTasks("1 edited");
        assertItemsLeft(1);

        filterActive();
        assertTasks("1 edited");

        //complete
        toggle("1 edited");
        assertNoTasks();

        filterCompleted();
        assertTasks("1 edited");

        filterAll();
        assertTasks("1 edited");

        delete("1 edited");
        assertNoTasks();
    }

    @Test
    public void testFilterFromAllToCompleted() {
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED));

        filterCompleted();
        assertTasks("2");
        assertItemsLeft(1);
    }

    @Test
    public void testFilterFromActiveToAll() {
        givenAtActive(aTask("1", ACTIVE), aTask("2", COMPLETED));

        filterAll();
        assertTasks("1", "2");
        assertItemsLeft(1);
    }

    @Test
    public void testFilterFromCompletedToActive() {
        givenAtCompleted(aTask("1", ACTIVE), aTask("2", COMPLETED));

        filterActive();
        assertTasks("1");
        assertItemsLeft(1);
    }

}
