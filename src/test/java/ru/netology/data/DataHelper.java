package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {

    private static Faker faker = new Faker();

    public static String getInvalidPassword() {
        return faker.internet().password();
    }

    public static String getInvalidLogin() {
        return faker.name().firstName();
    }

    public static String getInvalidAuthCode() {
        return faker.number().digits(6);
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }


    @SneakyThrows
    public static String getAuthCodeFromDB() {
        var runner = new QueryRunner();
        var authCodeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            return (String) runner.query(conn, authCodeSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void cleanAuthCodeFromDB()  {
        var runner = new QueryRunner();
        var authCodeSQL = "DELETE FROM auth_codes;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            runner.update(conn, authCodeSQL);
        }
    }

    @SneakyThrows
    public static void cleanUsersFromDB()  {
        var runner = new QueryRunner();
        var authCodeSQL = "DELETE FROM users;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            runner.update(conn, authCodeSQL);
        }
    }

    @SneakyThrows
    public static void cleanCardsFromDB() {
        var runner = new QueryRunner();
        var authCodeSQL = "DELETE FROM cards;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            runner.update(conn, authCodeSQL);
        }
    }

    @SneakyThrows
    public static void cleanCardTransactionsFromDB() {
        var runner = new QueryRunner();
        var authCodeSQL = "DELETE FROM card_transactions;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            runner.update(conn, authCodeSQL);
        }
    }
}