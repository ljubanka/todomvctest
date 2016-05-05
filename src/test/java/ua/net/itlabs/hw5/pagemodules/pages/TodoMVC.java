package ua.net.itlabs.hw5.pagemodules.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class TodoMVC {
    public enum TaskType {
        ACTIVE("false"),
        COMPLETED("true");

        public final String isCompletedValue;

        TaskType(String isCompletedValue) {
            this.isCompletedValue = isCompletedValue;
        }

        public String toString() {
            return this.isCompletedValue;
        }
    }

    public static ElementsCollection tasks = $$("#todo-list>li");

    public static void ensurePageOpened() {
        if (! url().equals("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }

    public static class Task {
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

    public static Task aTask(String name, TaskType type) {
        return new Task(name, type);
    }

    public static void givenAtAll(Task... tasks) {
        ensurePageOpened();

        String strJS = "localStorage.setItem(\"todos-troopjs\", \"[  ";
        for (Task task : tasks) {
            strJS += task;
        }
        strJS = strJS.substring(0, strJS.length()-2);
        strJS = strJS + "]\")";
        executeJavaScript(strJS);
        refresh();
    }

    public static void givenAtAll(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
    }

    public static void givenAtActive(Task... tasks) {
        givenAtAll(tasks);
        filterActive();
    }

    public static void givenAtActive(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
        filterActive();
    }

    public static void givenAtCompleted(Task... tasks) {
        givenAtAll(tasks);
        filterCompleted();
    }

    public static void givenAtCompleted(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
        filterCompleted();
    }

    public static Task[] aTasks(TaskType taskType, String... taskTexts) {
        Task tasksArray[] = new Task[taskTexts.length];
        for (int i=0; i<taskTexts.length; i++) {
            tasksArray[i] = aTask(taskTexts[i], taskType);
        }
        return tasksArray;
    }

    @Step
    public static void add(String... taskTexts) {
        for (String text: taskTexts) {
            $("#new-todo").shouldBe(enabled).setValue(text).pressEnter();
        }
    }

    @Step
    public static SelenideElement startEdit(String oldTask, String newTask) {
        tasks.find(exactText(oldTask)).doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newTask);
    }

    @Step
    public static void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().find(".destroy").click();
    }

    @Step
    public static void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    @Step
    public static void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public static void clearCompleted() {
        $("#clear-completed").click();
    }

    @Step
    public static void filterAll() {
        $(By.linkText("All")).click();
    }

    @Step
    public static void filterActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    public static void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    public static void assertTasks(String... taskTexts) {
        tasks.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    public static void assertNoTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    public static void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }
}
