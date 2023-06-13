package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;

public class AuthTest {

    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    @SneakyThrows
    public static void cleanUp() {
        DataHelper.cleanAuthCodeFromDB();
        DataHelper.cleanCardsFromDB();
        DataHelper.cleanCardTransactionsFromDB();
        DataHelper.cleanUsersFromDB();
    }

    // 1) Тест на корректную авторизацию

    @Test
    @SneakyThrows
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var loginPage = new LoginPage();
        var user = DataHelper.getFirstUserFromDB();
        var verificationPage = loginPage.validLogin(user.getLogin(), user.getPassword());
        var authCode = DataHelper.getAuthCodeFromDB();
        var dashboardPage = verificationPage.validVerify(authCode);
        dashboardPage.getHeading().shouldBe(exactText("Личный кабинет"));
    }

    // 2) Тест на неправильный password

    @Test
    @SneakyThrows
    void shouldLoginIfNotRegisteredActiveUserPassword() {
        var loginPage = new LoginPage();
        var user = DataHelper.getFirstUserFromDB();
        loginPage.invalidLogin(user.getLogin(), DataHelper.getInvalidPassword());
        errorNotification.shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    // 3) Тест на неправильный login

    @Test
    @SneakyThrows
    void shouldLoginIfNotRegisteredActiveUserLogin() {
        var loginPage = new LoginPage();
        var user = DataHelper.getFirstUserFromDB();
        loginPage.invalidLogin(DataHelper.getInvalidLogin(), user.getPassword());
        errorNotification.shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    // 4) Тест на неправильный auth_code

    @Test
    @SneakyThrows
    void shouldIncorrectAuthCode() {
        var loginPage = new LoginPage();
        var user = DataHelper.getFirstUserFromDB();
        var verificationPage = loginPage.validLogin(user.getLogin(), user.getPassword());
        var invalidAuthCode = DataHelper.getInvalidPassword();
        verificationPage.invalidVerify(invalidAuthCode);
        errorNotification.shouldHave(text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }

    // 5) Тест на блокировку приложения после 3-ех некорректных попыток авторизации

    @Test
    @SneakyThrows
    void shouldBlockedAppAfterLimitTries() {
        var loginPage = new LoginPage();
        var user = DataHelper.getFirstUserFromDB();
        var verificationPage = loginPage.validLogin(user.getLogin(), user.getPassword());
        var invalidAuthCode = DataHelper.getInvalidPassword();
        verificationPage.invalidVerify(invalidAuthCode);
        errorNotification.shouldHave(text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
        open("http://localhost:9999");
        var loginPage2 = new LoginPage();
        var user2 = DataHelper.getFirstUserFromDB();
        var verificationPage2 = loginPage2.validLogin(user2.getLogin(), user2.getPassword());
        var invalidAuthCode2 = DataHelper.getInvalidPassword();
        verificationPage2.invalidVerify(invalidAuthCode2);
        errorNotification.shouldHave(text("Ошибка! Превышено количество попыток ввода кода!"));
    }
}