package ua.net.itlabs;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;


public class TodoMVCPageWithClearedDataAfterEachTest extends BaseTest {



    @Before
    public void openPage() {
        if (! $("#new-todo").is(visible)) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");

    }

}
