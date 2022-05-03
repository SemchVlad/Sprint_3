package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.LoginResponse;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestCreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Успешное создание курьера с заполнением всех полей")
    @Description("Проверяет что курьер успешно создается POST-запросом /api/v1/courier и в ответе возвращается статус 201")
    public void createCourierSuccess() {
        Courier courier = new Courier("ninja" + new Random().nextInt(20), "1234", "saske");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then().statusCode(201).and().body("ok", equalTo(true));
        //удаляем тестового курьера
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверяет что POST-запрос /api/v1/courier возвращает отрицательный ответ, " +
            "если приходят данные по курьеру с уже существующим логином")
    public void unableToCreateSecondCourierWithSameLogin() {
        String login = "ninja" + new Random().nextInt(20);
        Courier courier1 = new Courier(login, "1234", "saske");
        Courier courier2 = new Courier(login, "12345", "naruto");
        given()
                .header("Content-type", "application/json")
                .body(courier1)
                .post("/api/v1/courier")
                .then().statusCode(201).and().body("ok", equalTo(true));
        given()
                .header("Content-type", "application/json")
                .body(courier2)
                .post("/api/v1/courier")
                .then().statusCode(409).and().body("code", equalTo(409))
                .and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        //удаляем тестового курьера
        deleteCourier(courier1);
    }

    @Test
    @DisplayName("Успешное создание курьера без фамилии")
    @Description("Проверяет что POST-запрос /api/v1/courier создает курьера без фамилии. Также проверяется успешный статус ответа.")
    public void CreateCourierWithOutFirstName() {
        Courier courier = new Courier("ninja" + new Random().nextInt(20), "1234");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then().statusCode(201).and().body("ok", equalTo(true));
        //удаляем тестового курьера
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Ошибка создания курьера, если неуказан логин курьера")
    @Description("Проверяет что запрос POST /api/v1/courier возвращает отрицательный ответ, " +
            "если не указан логин")
    public void unableToCreateCourierWithOutLogin() {
        Courier courier = new Courier(null, "1234", "saske");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then().statusCode(400).and().body("code", equalTo(400))
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка создания курьера, если неуказан пароль")
    @Description("Проверяет что POST-запрос /api/v1/courier возвращает отрицательный ответ, " +
            "если не указан пароль")
    public void unableToCreateCourierWithOutPassword() {
        Courier courier = new Courier("ninja" + new Random().nextInt(20), null, "saske");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then().statusCode(400).and().body("code", equalTo(400))
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Удаление тестового курьера")
    public void deleteCourier(Courier courier) {
        LoginResponse response = given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .post("/api/v1/courier/login")
                .as(LoginResponse.class);
        given().auth().none().delete("/api/v1/courier/" + response.getId())
                .then().statusCode(200).and().body("ok", equalTo(true));
    }
}


