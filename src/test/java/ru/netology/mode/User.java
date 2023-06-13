package ru.netology.mode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String login;
    private String password;

    public String getPassword() {
        if (getLogin().equals("vasya")) {
            return "qwerty123";
        }
        return "123qwerty";
    }
}
