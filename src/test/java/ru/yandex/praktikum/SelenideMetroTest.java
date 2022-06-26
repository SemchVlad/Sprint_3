package ru.yandex.praktikum;

import org.junit.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class SelenideMetroTest {


    // проверь, как работает выбор города
    @Test
    public void checkChooseCityDropdown() {

        // открой браузер
        // перейди на страницу стенда
        MetroHomePage metroPage = open("https://qa-metro.stand-2.praktikum-services.ru/", MetroHomePage.class);
        // дождись загрузки страницы
        metroPage.waitForLoadHomePage();
        // выбери Киев в списке городов
        metroPage.chooseCity("Киев");
        // проверь, что видна станция метро «Крещатик»
        $(byText("Крещатик")).shouldBe(visible);
    }

    // проверь отображение времени маршрута
    @Test
    public void checkRouteApproxTimeIsDisplayed() {
        // открой браузер
        // перейди на страницу стенда
        MetroHomePage metroPage =  open("https://qa-metro.stand-2.praktikum-services.ru/", MetroHomePage.class);
        // дождись загрузки страницы
        metroPage.waitForLoadHomePage();
        // построй маршрут от «Лубянки» до «Красногвардейской»
        metroPage.buildRoute("Лубянка","Красногвардейская");
        // проверь, что у первого маршрута списка отображается примерное время поездки, найди его по имени класса
        $$(byClassName("route-list-item__time")).get(0).shouldHave(text("≈ 36 мин."));
    }

    // проверь отображение станции «Откуда» в карточке маршрута
    @Test
    public void checkRouteStationFromIsCorrect() {
        // открой браузер
        // перейди на страницу стенда
        MetroHomePage metroPage = open("https://qa-metro.stand-2.praktikum-services.ru/", MetroHomePage.class);
        // дождись загрузки страницы
        metroPage.waitForLoadHomePage();
        // построй маршрут от «Лубянки» до «Красногвардейской»
        metroPage.buildRoute("Лубянка","Красногвардейская");
        // проверь, что отображается корректное название станции начала маршрута
        metroPage.getRouteStationFrom().shouldHave(text("Лубянка"));
    }

    // проверь отображение станции «Куда» в карточке маршрута
    @Test
    public void checkRouteStationToIsCorrect() {
        // открой браузер
        // перейди на страницу стенда
        MetroHomePage metroPage = open("https://qa-metro.stand-2.praktikum-services.ru/", MetroHomePage.class);
        // дождись загрузки страницы
        metroPage.waitForLoadHomePage();
        // построй маршрут от «Лубянки» до «Красногвардейской»
        metroPage.buildRoute("Лубянка","Красногвардейская");
        // проверь, что отображается корректное название станции конца маршрута
        metroPage.getRouteStationTo().shouldHave(text("Красногвардейская"));

    }

}