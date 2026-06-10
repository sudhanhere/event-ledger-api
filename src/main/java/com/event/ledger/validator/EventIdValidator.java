package com.event.ledger.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventIdValidator implements ConstraintValidator<ValidEventId, String> {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 255;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9_-]+$";

    @Override
    public void initialize(ValidEventId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isValid = value.length() >= MIN_LENGTH && 
                         value.length() <= MAX_LENGTH && 
                         value.matches(VALID_PATTERN) &&
                         !value.contains(" ");

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            if (value.length() < MIN_LENGTH) {
                context.buildConstraintViolationWithTemplate(
                    "EventId must be at least " + MIN_LENGTH + " characters long"
                ).addConstraintViolation();
            } else if (value.length() > MAX_LENGTH) {
                context.buildConstraintViolationWithTemplate(
                    "EventId must not exceed " + MAX_LENGTH + " characters"
                ).addConstraintViolation();
            } else if (value.contains(" ")) {
                context.buildConstraintViolationWithTemplate(
                    "EventId cannot contain spaces"
                ).addConstraintViolation();
            } else {
                context.buildConstraintViolationWithTemplate(
                    "EventId contains invalid characters. Only alphanumeric characters, hyphens (-), and underscores (_) are allowed"
                ).addConstraintViolation();
            }
        }

        return isValid;
    }
}
