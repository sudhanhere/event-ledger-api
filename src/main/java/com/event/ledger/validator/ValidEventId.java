package com.event.ledger.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventIdValidator.class)
@Documented
public @interface ValidEventId {
    String message() default "Invalid eventId format. EventId must be 3-255 characters long and contain only alphanumeric characters, hyphens, and underscores";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
