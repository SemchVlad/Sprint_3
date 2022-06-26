package ru.yandex.praktikum3;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ParamTest {

    private final String color;
    private final String expectedString;

    public ParamTest(String color, String expectedString) {
        this.color = color;
        this.expectedString = expectedString;
    }

    @Parameterized.Parameters
    public static Object[][] dataForTest() {
        return new Object[][] {
                { "красный", "Произошёл взрыв!"},
                { "зелёный", "Фух! Осталось обрезать ещё один провод."},
                { "чёрный", "Отлично! Бомба почти обезврежена!"},
                { "жёлтый", "Ты не можешь обрезать провод, которого нет!"}
        };
    }

    @Test
    public void paramTest() {
        Bomb bomb = new Bomb();
        Assert.assertEquals(expectedString, bomb.cutTheWire(color));
        System.out.println("Аккуратно режь " + color + " провод. " + expectedString);
    }
}