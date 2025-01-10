package com.practice.filmorate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface AfterDecember28th1895 {
    String message() default "Недопустимая дата выпуска фильма";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
