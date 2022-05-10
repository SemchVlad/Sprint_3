package ru.yandex.praktikum;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.model.Order;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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
    public void setUp() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("src/test/resources/env.properties"));
        RestAssured.baseURI = props.getProperty("baseURI");;
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Проверяет что заказ успешно создается POST запросом //api/v1/orders и возвращается статус 201 с track")
    public void paramTest() {
        Faker faker = new Faker();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Order order = new Order(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                faker.number().randomDigitNotZero(),
                faker.phoneNumber().phoneNumber(),
                faker.number().numberBetween(1, 10),
                simpleDateFormat.format(faker.date().future(1, TimeUnit.DAYS)),
                faker.dune().quote(),
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
