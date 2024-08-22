package ru.ibs;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodPage extends BasePage {

    private final static String BASE_TYPE = "Фрукт";

    @FindBy(xpath = "//button[text()='Добавить']")
    private WebElement btnAdd;

    @FindBy(xpath = "//input[@id='name']")
    private WebElement nameField;

    @FindBy(xpath = "//select[@id='type']")
    private WebElement selectType;

    @FindBy(xpath = "//option[@value='FRUIT']")
    private WebElement selectTypeFruit;

    @FindBy(xpath = "//input[@id='exotic']")
    private WebElement exoticCheckbox;

    @FindBy(xpath = "//button[@id='save']")
    private WebElement btnSave;

    public FoodPage() {
        PageFactory.initElements(driver, this);
    }

    @Step("Нажатие кнопки 'Добавить'.")
    public FoodPage clickBtnAdd() {
        btnAdd.click();
        return this;
    }

    @Step("Заполнение полей формы добавления товара.")
    public FoodPage fillFieldForm(String name, String type, Boolean isExotic) {
        nameField.sendKeys(name);

        if (type.equalsIgnoreCase(BASE_TYPE)) {
            selectType.click();
            selectTypeFruit.click();
        }

        if (isExotic) {
            exoticCheckbox.click();
        }

        btnSave.click();
        return this;
    }

    @Step("Проверка добавленного товара {name} в таблице.")
    public void assertFormFields(String name, String type, Boolean isExotic) throws InterruptedException {
        Thread.sleep(1000);
        List<WebElement> rows = driver.findElements(By.xpath("//tr"));
        WebElement lastRow = rows.get(rows.size() - 1);
        List<WebElement> cells = lastRow.findElements(By.tagName("td"));

        Assertions.assertAll("Проверка полей таблицы",
                () -> assertEquals(name, cells.get(0).getText(), "Проверка поля 'Наименование'."),
                () -> assertEquals(type, cells.get(1).getText(), "Проверка поля 'Тип'."),
                () -> assertEquals(String.valueOf(isExotic), cells.get(2).getText(),
                        "Проверка поля 'Экзотический'.")
        );
    }
}
