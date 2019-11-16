import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {

    private int dateMinDaysDelivery = 3;
    private String baseUrl = "http://localhost:9999/";

    @DisplayName("Заказ карты с валидными данными с добавлением 2 дней к дате по умолчанию")
    @Test
    void CardDeliveryOrderValidDataAdditionOf2DaysToDefaultDate() {

        String date = dateValid(dateMinDaysDelivery +2);

        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement notification = $("[data-test-id=notification]");
        $("[data-test-id=notification]").waitUntil(visible,15000);
        $("[data-test-id=notification] .notification__title").should(Condition.exactText("Успешно!"));
        $("[data-test-id=notification] .notification__content").should(Condition.exactText("Встреча успешно забронирована на " + date));
    }

    @DisplayName("Заказ карты с валидными данными без изменения даты по умолчанию")
    @Test
    void CardDeliveryOrderValidData1ToDefaultDate() {
        String date = dateValid(dateMinDaysDelivery);
        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement notification = $("[data-test-id=notification]");
        $("[data-test-id=notification]").waitUntil(visible,15000);
        $("[data-test-id=notification] .notification__title").should(Condition.exactText("Успешно!"));
        $("[data-test-id=notification] .notification__content").should(Condition.exactText("Встреча успешно забронирована на " + date));
    }

    @DisplayName("Заказ карты с невалидной датой")
    @Test
    void CardDeliveryOrderNotValidData() {

        String date = dateValid(dateMinDaysDelivery -2);

        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=date]");
        name.$(By.className("input__sub")).shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @DisplayName("Заказ карты с невалидной датой из прошедших дней")
    @Test
    void CardDeliveryOrderNotValidPastData() {

        String date = pastDate(dateMinDaysDelivery -3);

        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=date]");
        name.$(By.className("input__sub")).shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @DisplayName("Заказ карты с названием города на английском языке")
    @Test
    void CardDeliveryOrderNotValidEnglishNameCity() {
        String date = dateValid(dateMinDaysDelivery);
        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Saint-Peterburg");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=city]");
        name.$(By.className("input__sub")).shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @DisplayName("Заказ карты в город не являющийся административным центром")
    @Test
    void CardDeliveryOrderCityNotAdministrativeCenter() {
        String date = dateValid(dateMinDaysDelivery);
        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Новокузнецк");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=city]");
        name.$(By.className("input__sub")).shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @DisplayName("Заказ карты с невалидным именем")
    @Test
    void CardDeliveryOrderТщеValidName() {
        String date = dateValid(dateMinDaysDelivery);
        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Ivanov Ivan");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=name]");
        name.$(By.className("input__sub")).shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @DisplayName("Заказ карты с невалидным нмоером телефонома")
    @Test
    void CardDeliveryOrderNotValidNumberPhone() {
        String date = dateValid(dateMinDaysDelivery);
        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+7999999999");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=phone]");
        name.$(By.className("input__sub")).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @DisplayName("Заказ карты с непроставленным чекбоксом")
    @Test
    void CardDeliveryOrderWithoutCheckbox() {
        String date = dateValid(dateMinDaysDelivery);
        open(getBaseUrl());
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        //$("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    public String dateValid (int addDays) {
        LocalDate futureDate = LocalDate.now().plusDays(addDays);
        return String.format("%02d.%02d.%d", futureDate.getDayOfMonth(), futureDate.getMonthValue(), futureDate.getYear());

    }

    public String pastDate(int daysSubstract) {
        LocalDate pastDate = LocalDate.now().minusDays(daysSubstract);
        return String.format("%02d.%02d.%d", pastDate.getDayOfMonth(), pastDate.getMonthValue(), pastDate.getYear());
    }

    public String getBaseUrl() {
        return baseUrl;
    }


}


