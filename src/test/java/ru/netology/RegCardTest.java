package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegCardTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void positive() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Иванов Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15)).getText();
        assertEquals("Успешно!\n" + "Встреча успешно забронирована на " + verificationDate, text);
    }

    @Test
    void positiveCombSurname() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15)).getText();
        assertEquals("Успешно!\n" + "Встреча успешно забронирована на " + verificationDate, text);
    }

    @Test
    void negativeNotSupportCity() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Ревда");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"city\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Доставка в выбранный город недоступна", text);
    }

    @Test
    void negativeNotCity() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"city\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void negativeNotDate() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE );
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"date\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Неверно введена дата", text);
    }

    @Test
    void negativeNotValideDate() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(-3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"date\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Заказ на выбранную дату невозможен", text);
    }

    @Test
    void negativeNotCorrectDate() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(-3).format(DateTimeFormatter.ofPattern("dd.MM."));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"date\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Неверно введена дата", text);
    }

    @Test
    void negativeNotCorrectName() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Ivanov Ivan");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"name\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void negativeNotName() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"name\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

//    @Test // не должно быть возможности ввести только фамилию или имя. должны быть обе части.
//    void negativeNotSurname() {
//        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
//        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
//        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Иван");
//        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
//        $x("//*[@data-test-id=\"agreement\"]").click();
//        $x("//*[@class=\"button__text\"]").click();
//        String text = $x("//*[@data-test-id=\"name\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
//        assertEquals("???", text);
//    }

    @Test
    void negativeNotCorrectPhone() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+7987654321");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"phone\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void negativeNotPhone() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"phone\"]//child::span[@class=\"input__sub\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void negativeNotAgree() {
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Мамин-Сибиряк Иван");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"agreement\"]").should(visible, Duration.ofSeconds(10)).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", text);
    }

}
