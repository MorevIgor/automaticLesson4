package ru.netology;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegCardTest {
    @Test
    void positiveTest(){

        open("http://localhost:9999");
        $x("//*[@data-test-id=\"city\"]//self::input").setValue("Москва");
        String verificationDate = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id=\"date\"]//self::input").doubleClick().sendKeys(Keys.DELETE + verificationDate);
        $x("//*[@data-test-id=\"name\"]//self::input").setValue("Иван Иванов");
        $x("//*[@data-test-id=\"phone\"]//self::input").setValue("+79876543210");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        String text = $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15)).getText();
        assertEquals("Успешно!\n" + "Встреча успешно забронирована на " + verificationDate, text);


    }
}
