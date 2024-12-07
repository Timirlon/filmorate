package com.practice.filmorate.validation;

import com.practice.filmorate.model.User;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private String validateAndGetFirstMessageTemplate(User user) {
        return validator.validate(user).stream()
                .findFirst()
                .orElseThrow()
                .getConstraintDescriptor()
                .getMessageTemplate();
    }

    @Test
    public void invalidEmailPatternTest() {
        User user = new User(1, "@IncorrectEmail.com", "admin", "boss", LocalDate.now());
        String expected = "Некорректный адрес эл.почты";

        String actual = validateAndGetFirstMessageTemplate(user);

        assertEquals(expected, actual);
    }

    @Test
    public void blankLoginTest() {
        User user = new User(1, "correctEmail@mail.com", "", "boss", LocalDate.now());
        String expected = "Логин не может быть пустым";

        String actual = validateAndGetFirstMessageTemplate(user);

        assertEquals(expected, actual);
    }

    @Test
    public void nullNameTest() {
        User user = new User(1, "correctEmail@mail.com", "admin", null, LocalDate.now());

        String expected = user.getLogin();
        String actual = user.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void blankNameTest() {
        User user = new User(1, "correctEmail@mail.com", "admin", "", LocalDate.now());

        String expected = user.getLogin();
        String actual = user.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void futureDateAsBirthdayTest() {
        User user = new User(1, "correctEmail@mail.com", "admin", "boss", LocalDate.of(2100, 1, 1));

        String expected = "Дата рождения не может быть в будущем";
        String actual = validateAndGetFirstMessageTemplate(user);

        assertEquals(expected, actual);
    }
}
