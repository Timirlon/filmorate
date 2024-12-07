package com.practice.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;
    @Email(message = "Некорректный адрес эл.почты")
    String email;
    @NotBlank(message = "Логин не может быть пустым")
    String login;
    String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;

        if (name == null || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
    }
}
