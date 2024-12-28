package com.practice.filmorate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.Month;

@Target(ElementType.FIELD)
public @interface AfterDecember28th1895 {
    LocalDate firstFilmRelease = LocalDate.of(1895, Month.DECEMBER, 28);

    String message();
}
