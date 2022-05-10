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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestCreateCourierTest {

    @Before
    public void setUp() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("src/test/resources/env.properties"));
        RestAssured.baseURI = props.getProperty("baseURI");
    }

    @Test
    @DisplayName("Успешное создание курьера с заполнением всех полей")
    @Description("Проверяет что курьер успешно создается POST-запросом /api/v1/courier и в ответе возвращается статус 201")
    public void createCourierSuccess() {
        Courier courier = new Courier("ninja" + UUID.randomUUID(), "1234", "saske");
        Response response = createCourier(courier);
        response.then().statusCode(201).and().body("ok", equalTo(true));

        //удаляем тестового курьера
        Response delCourierResponse = deleteCourier(courier);
        //убедимся что удаление данных выполнено
        delCourierResponse.then().statusCode(200).and().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверяет что POST-запрос /api/v1/courier возвращает отрицательный ответ, " +
            "если приходят данные по курьеру с уже существующим логином")
    public void unableToCreateSecondCourierWithSameLogin() {
        String login = "ninja" + UUID.randomUUID().toString();
        Courier courier1 = new Courier(login, "1234", "saske");
        Courier courier2 = new Courier(login, "12345", "naruto");

        Response response1 = createCourier(courier1);
        response1.then().statusCode(201).and().body("ok", equalTo(true));

        Response response2 = createCourier(courier2);
        response2.then().statusCode(409).and().body("code", equalTo(409))
                .and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        //удаляем тестового курьера
        deleteCourier(courier1);
    }

    @Test
    @DisplayName("Успешное создание курьера без фамилии")
    @Description("Проверяет что POST-запрос /api/v1/courier создает курьера без фамилии. Также проверяется успешный статус ответа.")
    public void createCourierWithOutFirstName() {
        Courier courier = new Courier("ninja" + UUID.randomUUID(), "1234");
        Response response = createCourier(courier);
        response.then().statusCode(201).and().body("ok", equalTo(true));

        //удаляем тестового курьера
        Response delCourierResponse = deleteCourier(courier);
        //убедимся что удаление данных выполнено
        delCourierResponse.then().statusCode(200).and().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка создания курьера, если неуказан логин курьера")
    @Description("Проверяет что запрос POST /api/v1/courier возвращает отрицательный ответ, " +
            "если не указан логин")
    public void unableToCreateCourierWithOutLogin() {
        Courier courier = new Courier(null, "1234", "saske");
        Response response = createCourier(courier);
        response.then().statusCode(400).and().body("code", equalTo(400))
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка создания курьера, если неуказан пароль")
    @Description("Проверяет что POST-запрос /api/v1/courier возвращает отрицательный ответ, " +
            "если не указан пароль")
    public void unableToCreateCourierWithOutPassword() {
        Courier courier = new Courier("ninja" + UUID.randomUUID(), null, "saske");
        Response response = createCourier(courier);
        response.then().statusCode(400).and().body("code", equalTo(400))
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Создание курьера")
    private Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("Удаление тестового курьера")
    private Response deleteCourier(Courier courier) {
        //получение id курьера для удаления
        LoginResponse response = given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .post("/api/v1/courier/login")
                .as(LoginResponse.class);
        //удаление курьера
        return given().auth().none().delete("/api/v1/courier/" + response.getId());
    }
}


