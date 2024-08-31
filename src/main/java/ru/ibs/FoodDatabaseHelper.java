package ru.ibs;

import io.qameta.allure.Step;

import java.sql.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodDatabaseHelper {
    private static final String DB_URL = "jdbc:h2:tcp://149.154.71.152:9092/mem:testdb";
    private static final String USER = "user";
    private static final String PASS = "pass";

    private static final Map<String, String> productTypeMap = Map.of(
            "Фрукт", "FRUIT",
            "Овощ", "VEGETABLE"
    );

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private static PreparedStatement prepareStatement(Connection conn, String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    @Step("Проверка, что товар {name} есть в базе данных")
    public static boolean isFoodInDatabase(String name, String type, Boolean isExotic) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, "SELECT * FROM food WHERE FOOD_NAME = ?")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Проверка наличия строки");
                assertEquals(name, rs.getString("FOOD_NAME"), "Проверка поля FOOD_NAME");
                assertEquals(productTypeMap.get(type), rs.getString("FOOD_TYPE"), "Проверка поля FOOD_TYPE");
                assertEquals(isExotic, rs.getInt("FOOD_EXOTIC") == 1, "Проверка поля FOOD_EXOTIC");
            }
        }
        return true;
    }

    @Step("Удаление товара {name} из базы данных")
    public static void deleteFoodFromDatabase(String name) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, "DELETE FROM food WHERE FOOD_NAME = ?")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    @Step("Проверка, что после удаления товар {name} отсутствует в базе данных")
    public static boolean isFoodDeletedFromDatabase(String name) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, "SELECT * FROM food WHERE FOOD_NAME = ?")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next();
            }
        }
    }
}
