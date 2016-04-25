package ua.net.itlabs.hw4;

import com.codeborne.selenide.*;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;
import ua.net.itlabs.TodoMVCPageWithClearedDataAfterEachTest;


import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ua.net.itlabs.hw4.TodoMVCTest.TaskTypes.ACTIVE;
import static ua.net.itlabs.hw4.TodoMVCTest.TaskTypes.COMPLETED;


public class TodoMVCTest extends TodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testTasksCommonFlow() {

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
        tasks.shouldBe(empty);
    }

    @Test
    public void testAddAtActive() {
        //given - task at Active filter (in order to have filters panel visible)
        givenAllActive("1");
        filterActive();

        add("2");
        assertTasks("1", "2");
        assertItemsLeft(2);
    }

    @Test
    public void testEditAtActive() {
        //given - task at Active filter
        givenAllActive("1");
        filterActive();

        startEdit("1", "1 edited").pressEnter();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteAtActive() {
        //given - task at Active filter
        givenAllActive("1");
        filterActive();

        delete("1");
        assertNoTasks();
    }

    @Test
    public void testDeleteAtCompleted() {
        //given - completed task at Completed filter
        givenAllCompleted("1");
        filterCompleted();

        delete("1");
        assertNoTasks();
    }

    @Test
    public void testCompleteAtAll() {
        //given - task at All filter
        givenAllActive("1");

        toggle("1");
        assertTasks("1");
        assertItemsLeft(1);
        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testReopenAtAll() {
        //given - completed task at All filter
        givenAllCompleted("1");

        toggle("1");
        assertTasks("1");
        assertItemsLeft(1);
        filterCompleted();
        assertNoTasks();
    }

    @Test
    public void testReopenAtCompleted() {
        //given - task at Completed filter
        givenAllCompleted("1");
        filterCompleted();

        toggle("1");
        assertNoTasks();
        filterActive();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testCompleteAllAtAll() {
        //given - two tasks at All filter
        givenAllActive("1", "2");

        toggleAll();
        assertNoTasks();
        assertItemsLeft(0);
        filterCompleted();
        assertTasks("1", "2");
    }

    @Test
    public void testCompleteAllAtActive() {
        //given - two tasks at Active filter
        givenAllActive("1", "2");
        filterActive();

        toggleAll();
        assertNoTasks();
        assertItemsLeft(0);
        filterCompleted();
        assertTasks("1", "2");
    }


    @Test
    public void testClearCompletedAtAll() {
        //given - three tasks at All filter
        given(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", COMPLETED));

        clearCompleted();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testClearCompletedAtActive() {
        //given - three tasks at Active filter
        given(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", COMPLETED));
        filterActive();

        clearCompleted();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testClearCompletedAtCompleted() {
        //given - three tasks at Completed filter
        given(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", COMPLETED));
        filterCompleted();

        clearCompleted();
        assertNoTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testCancelEditAtAll() {
        //given - task at All filter
        givenAllActive("1");

        startEdit("1", "1 cancel edit").pressEscape();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtCompleted() {
        //given - two tasks on Completed filter
        givenAllCompleted("1", "2");
        filterCompleted();

        toggleAll();
        assertNoTasks();
        assertItemsLeft(2);

    }

    @Test
    public void testFilterCompletedFromAll() {
        //given - two tasks on All filter
        given(aTask("1", ACTIVE), aTask("2", COMPLETED));

        filterCompleted();
        assertTasks("2");
        assertItemsLeft(1);

    }

    @Test
    public void testFilterAllFromActive() {
        //given - two tasks (only one visible on Active filter)
        given(aTask("1", ACTIVE), aTask("2", COMPLETED));
        filterActive();

        filterAll();
        assertTasks("1", "2");
        assertItemsLeft(2);

    }

    @Test
    public void testFilterActiveFromCompleted() {
        //given - two tasks (only one visible on Completed filter)
        given(aTask("1", ACTIVE), aTask("2", COMPLETED));
        filterCompleted();

        filterActive();
        assertTasks("1");
        assertItemsLeft(1);

    }

    //extra coverage

    @Test
    public void testEditClickOutsideAtActive() {

        //given - task at Active filter
        givenAllActive("1");
        filterActive();

        startEdit("1", "1 edited");
        $("#new-todo").click();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testEditClickTabAtAll() {

        //given - task at All filter
        givenAllActive("1");

        startEdit("1", "1 edited").pressTab();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteByEmptyAtAll() {
        //given - task at All filter
        givenAllActive("1");

        startEdit("1", "").pressEnter();
        assertNoTasks();
    }

    public enum TaskTypes {ACTIVE, COMPLETED};
    ElementsCollection tasks = $$("#todo-list>li");

    private class Task {
        String name;
        TaskTypes type;

        public Task(String name, TaskTypes type) {
            this.name = name;
            this.type = type;
        }
    }

    private Task aTask(String name, TaskTypes type) {
        return new Task(name, type);
    }

    private void givenAllActive(String... names) {
        String strJS = "localStorage.setItem(\"todos-troopjs\", \"[";
        for (String task : names) {
            strJS = strJS + "{\\\"completed\\\":false, \\\"title\\\":\\\"" + task + "\\\"}, ";
        }
        strJS = strJS.substring(0, strJS.length()-2);
        strJS = strJS + "]\")";
        executeJavaScript(strJS);
        refresh();
    }

    private void givenAllCompleted(String... names) {
        String strJS = "localStorage.setItem(\"todos-troopjs\", \"[";
        for (String task : names) {
            strJS = strJS + "{\\\"completed\\\":true, \\\"title\\\":\\\"" + task + "\\\"}, ";
        }
        strJS = strJS.substring(0, strJS.length()-2);
        strJS = strJS + "]\")";
        executeJavaScript(strJS);
        refresh();
    }

    private void given(Task... list) {
        String strJS = "localStorage.setItem(\"todos-troopjs\", \"[";
        for (Task task : list) {
            if (task.type == ACTIVE)
                strJS = strJS + "{\\\"completed\\\":false, \\\"title\\\":\\\"" + task.name + "\\\"}, ";
            else
                strJS = strJS + "{\\\"completed\\\":true, \\\"title\\\":\\\"" + task.name + "\\\"}, ";
        }
        strJS = strJS.substring(0, strJS.length()-2);
        strJS = strJS + "]\")";
        executeJavaScript(strJS);
        refresh();
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
        return tasks.find(Condition.cssClass("active")).find(".edit").setValue(newTask);

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
