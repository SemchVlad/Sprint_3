package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.LoginResponse;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RestCourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Успешное выполнение запроса логина")
    @Description("POST запрос /api/v1/courier/login успешно выполняется со заполненными полями и возвращается статус 200")
    public void courierLoginSuccess() {
        Courier courier = new Courier("ninja_l" + new Random().nextInt(20), "1234");
        createCourier(courier);
        //Выполняем авторизацию
        Response response = given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .post("/api/v1/courier/login");
        response.then().statusCode(200).and().body("id", notNullValue());

        //удаляем тестового курьера
        deleteCourier(response.as(LoginResponse.class).getId());
    }

    @Test
    @DisplayName("Ошибка 400, если не передан пароль")
    @Description("Проверяет что в ответе вернётся сообщение \"Недостаточно данных для входа\" с кодом 400")
    public void courierNotLoginWithOutPassword() {
        Courier courier = new Courier("ninja_l" + new Random().nextInt(20), null);

        //Выполняем авторизацию
        Response response = given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .post("/api/v1/courier/login");
        response.then().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка 400, если не передан логин")
    @Description("Проверяет что в ответе вернётся сообщение \"Недостаточно данных для входа\" с кодом 400")
    public void courierNotLoginWithOutLogin() {
        Courier courier = new Courier(null, "1234");

        //Выполняем авторизацию
        Response response = given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .post("/api/v1/courier/login");
        response.then().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Ошибка 404, если учетная запись не существует")
    @Description("Проверяет что в ответе вернётся сообщение \"Учетная запись не найдена\" с кодом 404")
    public void courierNotLogin() {
        Courier courier = new Courier("ninja_l" + new Random().nextInt(20) + "_2", "1234_2");
        //Выполняем авторизацию
        Response response = given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .post("/api/v1/courier/login");
        response.then().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Создание тестового курьера")
    public void createCourier(Courier courier) {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then().statusCode(201).and().body("ok", equalTo(true));
    }

    @Step("Удаление тестового курьера")
    public void deleteCourier(String id) {
        given().auth().none().delete("/api/v1/courier/" + id)
                .then().statusCode(200).and().body("ok", equalTo(true));
    }
}


