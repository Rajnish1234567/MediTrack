package com.airtribe.meditrack.validators;

import com.airtribe.meditrack.exception.InvalidDataException;

public final class PersonalDetailValidator {

    private PersonalDetailValidator() {
    }

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Name cannot be empty");
        }
    }

    public static void validateAge(int age) {
        if (age <= 0 || age > 120) {
            throw new InvalidDataException("Age must be between 1 and 120");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || phone.length() != 10) {
            throw new InvalidDataException("Phone must contain 10 digits");
        }
    }

}