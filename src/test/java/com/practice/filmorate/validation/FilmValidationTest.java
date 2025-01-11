package com.practice.filmorate.validation;

import com.practice.filmorate.model.Film;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;

public class FilmValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private String validateAndGetFirstMessageTemplate(Film film) {
        return validator.validate(film).stream()
                .findFirst()
                .orElseThrow()
                .getConstraintDescriptor()
                .getMessageTemplate();
    }

    @Test
    public void emptyNameTest() {
        Film film = new Film(1, "", "short description", LocalDate.of(2024, 1, 1), 120, null);
        String expected = "Название не может быть пустым";

        String actual = validateAndGetFirstMessageTemplate(film);

        assertEquals(expected, actual);
    }

    @Test
    public void lengthOfDescriptionMoreThan200Test() {
        String filmDescription = "A".repeat(201);
        Film film = new Film(1, "Зеленый слоник", filmDescription, LocalDate.of(2024, 1, 1), 120, null);
        String expected = "Описание фильма превышает лимит символов";

        String actual = validateAndGetFirstMessageTemplate(film);

        assertEquals(expected, actual);
    }

    @Test
    public void releaseDateIsBeforeDecember28th1895() {
        Film film = new Film(1, "Зеленый слоник", "Зеленый слоник", LocalDate.of(1895, Month.DECEMBER, 27), 120,  null);
        String expected = "Недопустимая дата выпуска фильма";

        String actual = validateAndGetFirstMessageTemplate(film);

        assertEquals(expected, actual);
    }

    @Test
    public void negativeDurationTest() {
        Film film = new Film(1, "Зеленый слоник", "Зеленый слоник", LocalDate.now(), -30, null);
        String expected = "Продолжительность фильма должна быть положительной";

        String actual = validateAndGetFirstMessageTemplate(film);

        assertEquals(expected, actual);
    }
}
