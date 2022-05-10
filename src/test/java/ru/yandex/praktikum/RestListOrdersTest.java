package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.model.Order;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class RestListOrdersTest {
    @Before
    public void setUp() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("src/test/resources/env.properties"));
        RestAssured.baseURI = props.getProperty("baseURI");
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяет что GET-запрос /api/v1/orders возвращает ответ со статусом 201 и не пустым списком")
    public void getListOrders() {
        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
        response.then().statusCode(200).and().body("orders", notNullValue(List.class));
    }
}
