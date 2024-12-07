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
    @NotNull
    int id;
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    @PastOrPresent
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
