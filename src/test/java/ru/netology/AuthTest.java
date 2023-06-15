package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    //@SneakyThrows
    public static void cleanUp() {
        DataHelper.cleanAuthCodeFromDB();
        DataHelper.cleanCardsFromDB();
        DataHelper.cleanCardTransactionsFromDB();
        DataHelper.cleanUsersFromDB();
    }

    // 1) Тест на корректную авторизацию

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getAuthCodeFromDB();
        verificationPage.validVerify(verificationCode);
    }

    // 2) Тест на неправильный password

    @Test
    void shouldLoginIfNotRegisteredActiveUserPassword() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage.invalidPassword(authInfo);
    }

    // 3) Тест на неправильный login

    @Test
    void shouldLoginIfNotRegisteredActiveUserLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage.invalidLogin(authInfo);
    }

    // 4) Тест на неправильный auth_code

    @Test
    void shouldIncorrectAuthCode() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerify();
    }

    // 5) Тест на блокировку приложения после 3-ех некорректных попыток авторизации

    @Test
    void shouldBlockedAppAfterLimitTries() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerify();
        open("http://localhost:9999");
        var loginPage2 = new LoginPage();
        var authInfo2 = DataHelper.getAuthInfo();
        VerificationPage verificationPage2 = loginPage2.validLogin(authInfo2);
        verificationPage2.blockedVerify();
    }
}