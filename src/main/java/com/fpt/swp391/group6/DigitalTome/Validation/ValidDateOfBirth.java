package com.fpt.swp391.group6.DigitalTome.Validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateOfBirth {

    String message() default "Date of birth must be greater than 1950 and less than current date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
