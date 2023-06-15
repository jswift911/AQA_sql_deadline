package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public VerificationPage() {
        codeField.shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {
        codeField.setValue(DataHelper.getAuthCodeFromDB());
        verifyButton.click();
        return new DashboardPage();
    }

    public void invalidVerify() {
        codeField.setValue(DataHelper.getInvalidAuthCode());
        verifyButton.click();
        getVerifyError();
    }

    public void blockedVerify() {
        codeField.setValue(DataHelper.getInvalidAuthCode());
        verifyButton.click();
        getVerifyError();
    }

    public void getVerifyError() {
        errorNotification.shouldHave(text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }

    public void getBlockedVerifyError() {
        errorNotification.shouldHave(text("Ошибка! Превышено количество попыток ввода кода!"));
    }
}