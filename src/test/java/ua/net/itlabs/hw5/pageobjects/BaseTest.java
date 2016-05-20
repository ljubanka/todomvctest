package ua.net.itlabs.hw5.pageobjects;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.BeforeClass;
import ru.yandex.qatools.allure.annotations.Attachment;
import ua.net.itlabs.hw5.pageobjects.pages.TodoMVCPage;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.ex.ErrorMessages.screenshot;

public class BaseTest {

    @After
    public void tearDown() throws IOException {
        screenshot();
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() throws IOException {
        File screenshot = Screenshots.getScreenShotAsFile();
        return Files.toByteArray(screenshot);
    }
}
