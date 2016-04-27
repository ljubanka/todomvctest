package ua.net.itlabs.hw4.v1;

import org.junit.After;
import org.junit.Before;
import ua.net.itlabs.hw4.v1.BaseTest;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;


public class TodoMVCPageWithClearedDataAfterEachTest extends BaseTest {



    @Before
    public void openPage() {
        if (! url().equals("https://todomvc4tasj.herokuapp.com/")) {//(! $("#new-todo").is(visible)) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");

    }

}
