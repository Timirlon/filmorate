package com.practice.filmorate.controller;

import com.practice.filmorate.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void createMethodShouldNotCreateFilmIfIncorrectNameIsGiven() {
        Film film = filmController.create(new Film(1, "", "short description", LocalDate.of(2024, 1, 1), 120));

        assertNull(film);
    }
}
