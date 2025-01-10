package com.practice.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Month;

public class ReleaseDateValidator implements ConstraintValidator<AfterDecember28th1895, LocalDate> {
    static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        return releaseDate.isAfter(FIRST_FILM_RELEASE_DATE);
    }

}
