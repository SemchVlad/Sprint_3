package ru.yandex.praktikum3;

public class Bomb {

    public String cutTheWire(String color) {
        switch (color){
            case ("красный"):
                return "Произошёл взрыв!";
            case ("зелёный"):
                return "Фух! Осталось обрезать ещё один провод.";
            case ("чёрный"):
                return "Отлично! Бомба почти обезврежена!";
            default:
                return "Ты не можешь обрезать провод, которого нет!";
        }
    }
}