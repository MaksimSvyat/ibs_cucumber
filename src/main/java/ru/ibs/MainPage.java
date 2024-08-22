package ru.ibs;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends BasePage {

    @FindBy(xpath = "//a[@href='#']")
    private WebElement sandbox;

    @FindBy(xpath = "//a[@href='/food']")
    private WebElement sandboxFood;

    public MainPage(String url) {
        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    @Step("Переход на страницу 'Товары'.")
    public void switchToFoodPage() {
        sandbox.click();
        sandboxFood.click();
    }
}
