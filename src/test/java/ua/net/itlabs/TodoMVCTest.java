package ua.net.itlabs;

import com.codeborne.selenide.*;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;


import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class TodoMVCTest extends TodoMVCPageWithClearedDataAfterEachTest {



    @Test
    public void testTasksCommonFlow() {

        add("1");
        startEdit("1", "1 edited").pressEnter();
        assertTasks("1 edited");

        filterActive();
        assertTasks("1 edited");
        add("2");
        assertTasks("1 edited", "2");

        //complete
        toggle("2");
        assertTasks("1 edited");
        assertItemsLeft(1);

        //complete all
        toggleAll();
        assertNoTasks();

        filterCompleted();
        assertTasks("1 edited", "2");

        //reopen
        toggle("2");
        assertTasks("1 edited");
        clearCompleted();
        assertNoTasks();

        filterAll();
        assertTasks("2");

        delete("2");
        assertNoTasks();
        tasks.shouldBe(empty);
    }

    @Test
    public void testCancelEditAtAll() {
        //given - task at All filter
        add("1");

        startEdit("1", "1 cancel edit").pressEscape();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtCompleted() {
        //given - two tasks on Completed filter
        add("1", "2");
        toggleAll();
        filterCompleted();

        toggleAll();
        assertNoTasks();
        assertItemsLeft(2);

    }

    //extra coverage

    @Test
    public void testEditClickOutsideAtActive() {

        //given - task at Active filter
        add("1");
        filterActive();

        startEdit("1", "1 edited");
        $("#new-todo").click();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testEditClickTabAtAll() {

        //given - task at All filter
        add("1");

        startEdit("1", "1 edited").pressTab();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteByEmptyAtAll() {
        //given - task at All filter
        add("1");

        startEdit("1", "").pressEnter();
        assertNoTasks();
    }



    ElementsCollection tasks = $$("#todo-list>li");

    private void givenAllActive(String taskType, String... tasks) {
        if (taskType.equals("active")) {
            for (String task: tasks) {
                executeJavaScript(String.format("localStorage.setItem(\"todos-troopjs\", \"[{\"completed\":false, \"title\":%s}]\")"), task);
            }
        }
        else {
            for (String task: tasks) {
                executeJavaScript(String.format("localStorage.setItem(\"todos-troopjs\", \"[{\"completed\":true, \"title\":%s}]\")"), task);
            }
        }

    }

    @Step
    private void add(String... taskTexts) {
        for (String text: taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
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
