package com.fpt.swp391.group6.DigitalTome.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Calendar;
import java.util.Date;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, Date> {
    @Override
    public void initialize(ValidDateOfBirth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if(date == null){
            return true;
        }

        Calendar minDateOfBirth = Calendar.getInstance();
        minDateOfBirth.set(1950, Calendar.JANUARY, 1);

        Calendar currentDate  = Calendar.getInstance();

        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.setTime(date);

        return dateOfBirth.after(minDateOfBirth) && dateOfBirth.before(currentDate);

    }
}
