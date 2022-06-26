package ru.yandex.praktikum2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockTest {

    @Mock
    private Cat cat;
    @Mock
    private Dog dog;

//    @Test
//    public void test() {
//        Cat halfCat = cat.createHalfCat();
//        Dog halfDog = dog.createHalfDog();
//        Mockito.verify(cat,  Mockito.times(1)).createHalfCat(); // проверь, что createHalfCat() вызвался один раз
//        Mockito.verify(dog,  Mockito.times(1)).createHalfDog(); // проверь, что createHalfDog() вызвался один раз
//    }

    @Test
    public void test() {
        Mockito.when(cat.createHalfCat()).thenReturn(new Cat(2, "Я самый умный"));;// стаб, возвращающий половину кота с параметрами - 2 лапы, говорит "Я самый умный"
        Mockito.when(dog.createHalfDog()).thenReturn(new Dog(2, "Я самый весёлый"));// стаб, возвращающий половину пса с параметрами - 2 лапы, говорит "Я самый весёлый"
        Cat halfCat = cat.createHalfCat();
        Dog halfDog = dog.createHalfDog();
        CatDog catDog = new CatDog(halfCat, halfDog);
        Assert.assertEquals(4, catDog.getLegsCount());// проверка, что у Котопса 4 лапы
        Assert.assertEquals("Единственный в мире малыш Котопёс", catDog.getSound());// проверка, что Котопёс говорит "Единственный в мире малыш Котопёс"
    }
}