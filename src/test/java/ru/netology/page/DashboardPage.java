package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {

    private final SelenideElement heading = $("h2.heading");

    public SelenideElement getHeading() {
        return heading;
    }
}