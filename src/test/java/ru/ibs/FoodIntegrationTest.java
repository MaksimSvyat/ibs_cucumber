package ru.ibs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestListener.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FoodIntegrationTest extends BaseTest {

    @Owner("Maks jivern")
    @Description("Проверка функционала добавления товаров, валидация отображения товаров в списке и сохранения " +
            "записи в базе данных.")
    @ParameterizedTest
    @MethodSource("getParameters")
    public void checkAddFood(String name, String type, Boolean isExotic) throws InterruptedException, SQLException {
        FoodPage foodPage = new FoodPage();
        foodPage.clickBtnAdd()
                .fillFieldForm(name, type, isExotic)
                .assertFormFields(name, type, isExotic);

        assertTrue(FoodDatabaseHelper.isFoodInDatabase(name, type, isExotic));
        FoodDatabaseHelper.deleteFoodFromDatabase(name);
        assertTrue(FoodDatabaseHelper.isFoodDeletedFromDatabase(name));
    }

    public Stream<Arguments> getParameters() {
        Config config = ConfigFactory.load("app.conf");
        List<? extends ConfigObject> params = config.getObjectList("testData");
        return params.stream().map(param -> Arguments.of(
                param.get("name").unwrapped(),
                param.get("type").unwrapped(),
                param.get("isExotic").unwrapped()
        ));
    }
}
