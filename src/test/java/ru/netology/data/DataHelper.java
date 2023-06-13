package ru.netology.data;

import com.github.javafaker.Faker;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import ru.netology.mode.User;

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

    public static User getFirstUserFromDB() throws SQLException {
        var runner = new QueryRunner();
        var usersSQL = "SELECT login,password FROM users;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            return runner.query(conn, usersSQL, new BeanHandler<>(User.class));
        }
    }

    public static String getAuthCodeFromDB() throws SQLException {
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

    public static void cleanAuthCodeFromDB() throws SQLException {
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

    public static void cleanUsersFromDB() throws SQLException {
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

    public static void cleanCardsFromDB() throws SQLException {
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

    public static void cleanCardTransactionsFromDB() throws SQLException {
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