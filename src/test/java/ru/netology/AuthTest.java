package ru.netology;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.mode.User;

import java.sql.DriverManager;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    // 1) Тест на успешную авторизацию

    @Test
    @SneakyThrows
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var runner = new QueryRunner();
        var usersSQL = "SELECT login,password FROM users;";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            var firstUser = runner.query(conn, usersSQL, new BeanHandler<>(User.class));
            $("[data-test-id=login] input").setValue(firstUser.getLogin());
            $("[data-test-id=password] input").setValue("123qwerty");
            $("[data-test-id=action-login]").click();
            $("[data-test-id=code] input").shouldBe(visible);
            var authCodeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
            var authCode = runner.query(conn, authCodeSQL, new ScalarHandler<>());
            $("[data-test-id=code] input").setValue((String) authCode);
            $("[data-test-id=action-verify]").click();
            $("h2.heading").shouldHave(exactText("Личный кабинет"));
        }
    }

    // 2) Тест на неверный пароль

    @Test
    @SneakyThrows
    void shouldLoginIfNotRegisteredActiveUserPassword() {
        var faker = new Faker();
        var runner = new QueryRunner();
        var usersSQL = "SELECT login,password FROM users;";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            var firstUser = runner.query(conn, usersSQL, new BeanHandler<>(User.class));
            $("[data-test-id=login] input").setValue(firstUser.getLogin());
            $("[data-test-id=password] input").setValue(faker.internet().password());
            $("[data-test-id=action-login]").click();
            $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
        }
    }

    // 3) Тест на неверный логин

    @Test
    @SneakyThrows
    void shouldLoginIfNotRegisteredActiveUserLogin() {
        var faker = new Faker();
        var runner = new QueryRunner();
        var usersSQL = "SELECT login,password FROM users;";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            var firstUser = runner.query(conn, usersSQL, new BeanHandler<>(User.class));
            $("[data-test-id=login] input").setValue(faker.name().firstName());
            $("[data-test-id=password] input").setValue("123qwerty");
            $("[data-test-id=action-login]").click();
            $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
        }
    }

    // 4) Тест на неправильный код верификации

    @Test
    @SneakyThrows
    void shouldIncorrectAuthCode() {
        var faker = new Faker();
        var runner = new QueryRunner();
        var usersSQL = "SELECT login,password FROM users;";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            var firstUser = runner.query(conn, usersSQL, new BeanHandler<>(User.class));
            $("[data-test-id=login] input").setValue(firstUser.getLogin());
            $("[data-test-id=password] input").setValue("123qwerty");
            $("[data-test-id=action-login]").click();
            $("[data-test-id=code] input").shouldBe(visible);
            $("[data-test-id=code] input").setValue(faker.number().digits(6));
            $("[data-test-id=action-verify]").click();
            $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
        }
    }
}
