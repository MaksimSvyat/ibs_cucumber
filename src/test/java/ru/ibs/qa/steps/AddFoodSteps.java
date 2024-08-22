package ru.ibs.qa.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.ibs.BasePage;
import ru.ibs.FoodDatabaseHelper;
import ru.ibs.FoodPage;
import ru.ibs.MainPage;

import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddFoodSteps {

    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Given("на странице по адресу {string} перешёл в раздел Песочница - Товары")
    public void navigateToSandboxFoodPage(String url) {
        BasePage.setDriver(driver);
        MainPage mainPage = new MainPage(url);
        mainPage.switchToFoodPage();
    }

    @When("нажимаю кнопку Добавить")
    public void clickAddButton() {
        FoodPage foodPage = new FoodPage();
        foodPage.clickBtnAdd();
    }

    @When("ввожу данные в форму добавления товаров и нажимаю кнопку Сохранить")
    public void fillInProductFormAndSave(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String name = row.get("name");
            String type = row.get("type");
            Boolean isExotic = Boolean.valueOf(row.get("isExotic"));

            FoodPage foodPage = new FoodPage();
            foodPage.fillFieldForm(name, type, isExotic);
        }
    }

    @Then("должен увидеть товар в списке с данными {string}, {string}, {string}")
    public void verifyProductInList(String name, String type, String isExotic) throws InterruptedException {
        FoodPage foodPage = new FoodPage();
        foodPage.assertFormFields(name, type, Boolean.valueOf(isExotic));
    }

    @Then("проверяю добавление товара в базу данных {string}, {string}, {string}")
    public void verifyProductAddedToDatabase(String name, String type, String isExotic) throws SQLException {
        assertTrue(FoodDatabaseHelper.isFoodInDatabase(name, type, Boolean.valueOf(isExotic)));
    }

    @When("удаляю из базы данных добавленный товар {string}")
    public void deleteProductFromDatabase(String name) throws SQLException {
        FoodDatabaseHelper.deleteFoodFromDatabase(name);
    }

    @Then("проверяю удаление товара {string} из базы данных")
    public void verifyProductDeletedFromDatabase(String name) throws SQLException {
        assertTrue(FoodDatabaseHelper.isFoodDeletedFromDatabase(name));
    }

    @After()
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
