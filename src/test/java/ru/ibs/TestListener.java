package ru.ibs;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.Optional;

public class TestListener implements TestWatcher {

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        BasePage.driver.close();
        BasePage.driver.quit();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        BasePage.driver.close();
        BasePage.driver.quit();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        BasePage.driver.close();
        BasePage.driver.quit();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        Allure.getLifecycle().addAttachment("screenshot", "img/png", "png"
                , ((TakesScreenshot) BasePage.driver).getScreenshotAs(OutputType.BYTES));
        BasePage.driver.close();
        BasePage.driver.quit();
    }
}
