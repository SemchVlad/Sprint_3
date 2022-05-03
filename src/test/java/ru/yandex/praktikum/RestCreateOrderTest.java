package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.model.Order;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class RestCreateOrderTest {
    private boolean grey;
    private boolean black;

    public RestCreateOrderTest(boolean grey, boolean black) {
        this.grey = grey;
        this.black = black;
    }

    @Parameterized.Parameters(name = "{index}: Order(Grey={0}, Black={1})")
    public static Iterable<Object[]> dataForTest() {
        return Arrays.asList(new Object[][]{
                {true, true},
                {true, false},
                {false, false},
                {false, true}
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Проверяет что заказ успешно создается POST запросом //api/v1/orders и возвращается статус 201 с track")
    public void paramTest() {
        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                getArrayString(grey, black)
        );
        Response response = given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders");
        response.then().statusCode(201).and().body("track", notNullValue());
    }

    private ArrayList<String> getArrayString(boolean grey, boolean black) {
        ArrayList<String> result = new ArrayList<String>();
        if (grey) result.add("GREY");
        if (black) result.add("BLACK");
        return result;
    }
}
