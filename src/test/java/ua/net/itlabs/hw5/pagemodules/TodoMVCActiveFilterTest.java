package ua.net.itlabs.hw5.pagemodules;

import org.junit.Test;

import static com.codeborne.selenide.Selenide.$;
import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.*;
import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pagemodules.pages.TodoMVC.TaskType.COMPLETED;

public class TodoMVCActiveFilterTest {
    @Test
    public void testAdd() {
        givenAtActive(ACTIVE, "1");

        add("2");
        assertTasks("1", "2");
        assertItemsLeft(2);
    }

    @Test
    public void testEdit() {
        givenAtActive(ACTIVE, "1", "2");

        startEdit("2", "2 edited").pressEnter();
        assertTasks("1", "2 edited");
        assertItemsLeft(2);
    }

    @Test
    public void testDelete() {
        givenAtActive(ACTIVE, "1");

        delete("1");
        assertNoTasks();
    }

    @Test
    public void testCompleteAll() {
        givenAtActive(ACTIVE, "1", "2");

        toggleAll();
        assertNoTasks();
        assertItemsLeft(0);
    }

    @Test
    public void testClearCompleted() {
        givenAtActive(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", COMPLETED));

        clearCompleted();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testEditClickOutside() {
        givenAtActive(ACTIVE, "1", "2");

        startEdit("2", "2 edited");
        $("#new-todo").click();
        assertTasks("1", "2 edited");
        assertItemsLeft(2);
    }
}
