package ua.net.itlabs.hw4.v3;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static ua.net.itlabs.hw4.v3.TodoMVCTest.TaskType.ACTIVE;
import static ua.net.itlabs.hw4.v3.TodoMVCTest.TaskType.COMPLETED;

public class TodoMVCTest extends BaseTest {
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
    public void testAddAtActive() {
        givenAtActive(ACTIVE, "1");

        add("2");
        assertTasks("1", "2");
        assertItemsLeft(2);
    }

    @Test
    public void testEditAtActive() {
        givenAtActive(ACTIVE, "1", "2");

        startEdit("2", "2 edited").pressEnter();
        assertTasks("1", "2 edited");
        assertItemsLeft(2);
    }

    @Test
    public void testDeleteAtActive() {
        givenAtActive(ACTIVE, "1");

        delete("1");
        assertNoTasks();
    }

    @Test
    public void testDeleteAtCompleted() {
        givenAtCompleted(COMPLETED, "1", "2");

        delete("2");
        assertTasks("1");
        assertItemsLeft(0);
    }

    @Test
    public void testCompleteAtAll() {
        givenAtAll(ACTIVE, "1", "2");

        toggle("2");
        assertTasks("1", "2");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAtAll() {
        givenAtAll(COMPLETED, "1", "2");

        toggle("2");
        assertTasks("1", "2");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAtCompleted() {
        givenAtCompleted(COMPLETED, "1");

        toggle("1");
        assertNoTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testCompleteAllAtAll() {
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", ACTIVE));

        toggleAll();
        assertTasks("1", "2", "3");
        assertItemsLeft(0);
    }

    @Test
    public void testCompleteAllAtActive() {
        givenAtActive(ACTIVE, "1", "2");

        toggleAll();
        assertNoTasks();
        assertItemsLeft(0);
    }


    @Test
    public void testClearCompletedAtAll() {
        givenAtAll(COMPLETED, "1", "2", "3");

        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testClearCompletedAtActive() {
        givenAtActive(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", COMPLETED));

        clearCompleted();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testClearCompletedAtCompleted() {
        givenAtCompleted(COMPLETED, "1");

        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testCancelEditAtAll() {
        givenAtAll(ACTIVE, "1");

        startEdit("1", "1 cancel edit").pressEscape();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtCompleted() {
        givenAtCompleted(COMPLETED, "1", "2");

        toggleAll();
        assertNoTasks();
        assertItemsLeft(2);

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

    @Test
    public void testEditClickOutsideAtActive() {
        givenAtActive(ACTIVE, "1", "2");

        startEdit("2", "2 edited");
        $("#new-todo").click();
        assertTasks("1", "2 edited");
        assertItemsLeft(2);
    }

    @Test
    public void testEditClickTabAtAll() {
        givenAtAll(ACTIVE, "1", "2", "3");

        startEdit("2", "2 edited").pressTab();
        assertTasks("1", "2 edited", "3");
        assertItemsLeft(3);
    }

    @Test
    public void testDeleteByEmptyAtAll() {
        givenAtAll(ACTIVE, "1", "2", "3");

        startEdit("2", "").pressEnter();
        assertTasks("1", "3");
    }

    public enum TaskType {
        ACTIVE("false"),
        COMPLETED("true");

        private final String isCompletedValue;

        TaskType(String isCompletedValue) {
            this.isCompletedValue = isCompletedValue;
        }

        private String isCompletedValue() {
            return isCompletedValue;
        }

        public String toString() {
            return this.isCompletedValue;
        }
    }

    ElementsCollection tasks = $$("#todo-list>li");

    private void ensurePageOpened() {
        if (! url().equals("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }

    private class Task {
        String name;
        TaskType type;

        public Task(String name, TaskType type) {
            this.name = name;
            this.type = type;
        }

        public String toString() {
            return "{\\\"completed\\\":" + type + ", \\\"title\\\":\\\"" + name + "\\\"}, ";
        }
    }

    private Task aTask(String name, TaskType type) {
        return new Task(name, type);
    }

    private void givenAtAll(Task... tasks) {
        ensurePageOpened();

        String strJS = "localStorage.setItem(\"todos-troopjs\", \"[";
        if (tasks.length != 0) {
            for (Task task : tasks) {
                strJS += task;//"{\\\"completed\\\":" + task.type + ", \\\"title\\\":\\\"" + task + "\\\"}, ";
            }
            strJS = strJS.substring(0, strJS.length()-2);
        }
        strJS = strJS + "]\")";
        executeJavaScript(strJS);
        refresh();
    }

    private void givenAtAll(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
    }

    private void givenAtActive(Task... tasks) {
        givenAtAll(tasks);
        filterActive();
    }

    private void givenAtActive(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
        filterActive();
    }

    private void givenAtCompleted(Task... tasks) {
        givenAtAll(tasks);
        filterCompleted();
    }

    private void givenAtCompleted(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
        filterCompleted();
    }

    private Task[] aTasks(TaskType taskType, String... taskTexts) {
        Task tasksArray[] = new Task[taskTexts.length];
        for (int i=0; i<taskTexts.length; i++) {
            tasksArray[i] = aTask(taskTexts[i], taskType);
        }
        return tasksArray;
    }

    @Step
    private void add(String... taskTexts) {
        for (String text: taskTexts) {
            $("#new-todo").shouldBe(enabled).setValue(text).pressEnter();
        }
    }

    @Step
    private SelenideElement startEdit(String oldTask, String newTask) {
        tasks.find(exactText(oldTask)).doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newTask);
    }

    @Step
    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().find(".destroy").click();
    }

    @Step
    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    @Step
    private void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    private void clearCompleted() {
        $("#clear-completed").click();
    }

    @Step
    private void filterAll() {
        $(By.linkText("All")).click();
    }

    @Step
    private void filterActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    private void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    private void assertTasks(String... taskTexts) {
        tasks.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    private void assertNoTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    private void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }
}
