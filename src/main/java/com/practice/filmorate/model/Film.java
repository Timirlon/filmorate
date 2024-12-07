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
    final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 25);

    @NotNull
    int id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @PastOrPresent
    LocalDate releaseDate;
    @Positive
    int duration;
    @AssertTrue
    boolean isAfterDecember28th1895;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;

        this.isAfterDecember28th1895 = releaseDate.isAfter(FIRST_FILM_RELEASE_DATE);
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        this.isAfterDecember28th1895 = releaseDate.isAfter(FIRST_FILM_RELEASE_DATE);
    }
}
