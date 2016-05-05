package ua.net.itlabs.hw5.pageobjects;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.ACTIVE;
import static ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage.TaskType.COMPLETED;

public class TodoMVCCompletedFilterTest {
<<<<<<< HEAD

    TodoMVCPage page = new TodoMVCPage();

=======
    static TodoMVCPage page;

    @BeforeClass
    public static void PageInitialize() {
        page = new TodoMVCPage();
    }
    
>>>>>>> bc3b38efc0824033c4d0f798698d53718212a774
    @Test
    public void testDelete() {
        page.givenAtCompleted(COMPLETED, "1", "2");

        page.delete("2");
        page.assertTasks("1");
        page.assertItemsLeft(0);
    }

    @Test
    public void testReopen() {
        page.givenAtCompleted(COMPLETED, "1");

        page.toggle("1");
        page.assertNoTasks();
        page.assertItemsLeft(1);
    }

    @Test
    public void testClearCompleted() {
        page.givenAtCompleted(COMPLETED, "1");

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void testReopenAll() {
        page.givenAtCompleted(COMPLETED, "1", "2");

        page.toggleAll();
        page.assertNoTasks();
        page.assertItemsLeft(2);
    }
}
