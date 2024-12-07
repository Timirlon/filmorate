package com.practice.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.Month;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    int id;

    @NotBlank(message = "Название не может быть пустым")
    String name;

    @Size(max = 200, message = "Описание фильма превышает лимит символов")
    String description;

    LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    int duration;

    @AssertTrue(message = "Недопустимая дата выпуска фильма")
    boolean isAfterDecember28th1895;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;

        isAfterDecember28th1895 = releaseDate.isAfter(FIRST_FILM_RELEASE_DATE);
    }

    private void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        isAfterDecember28th1895 = releaseDate.isAfter(FIRST_FILM_RELEASE_DATE);
    }
}
