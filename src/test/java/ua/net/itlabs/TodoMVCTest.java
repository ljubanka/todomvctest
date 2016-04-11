package ua.net.itlabs;

import com.codeborne.selenide.*;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;


import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class TodoMVCTest {

        @After
        public void tearDown() throws IOException {
            screenshot();
        }

        @Attachment(type = "image/png")
        public byte[] screenshot() throws IOException {
            File screenshot = Screenshots.takeScreenShotAsFile();
            return Files.toByteArray(screenshot);
        }

    @Before
    public void openPage() {
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");

    }


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

        //given Active filter
        add("1");
        filterActive();

        startEdit("1", "1 edited");
        $("#new-todo").click();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testEditClickTabAtAll() {

        add("1");
        startEdit("1", "1 edited").pressTab();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteByEmptyAtAll() {

        add("1");
        startEdit("1", "").pressEnter();
        assertNoTasks();
    }



    ElementsCollection tasks = $$("#todo-list>li");

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

    @Step
    private void assertTasks(String... taskTexts) {
        tasks.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    @Step
    private void assertNoTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    @Step
    private void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }

}
