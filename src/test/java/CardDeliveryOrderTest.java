import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {

    private int dateMinDaysDelivery = 3;

    @Test
    void CardDeliveryOrderValidData() {

        String date = dateValid(dateMinDaysDelivery +2);

        open("http://localhost:9999/");
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
        //$("[data-test-id=notification] .notification__content").should(Condition.exactText("Встреча успешно забронирована на " + date));
        $("[data-test-id=notification] .notification__content").should(Condition.exactText("Встреча успешно забронирована на " + date));
    }




    public String dateValid (int addDays) {
        LocalDate futureDate = LocalDate.now().plusDays(addDays);
        return String.format("%02d.%02d.%d", futureDate.getDayOfMonth(), futureDate.getMonthValue(), futureDate.getYear());

    }
}


