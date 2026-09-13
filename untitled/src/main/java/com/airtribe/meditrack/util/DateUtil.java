package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private DateUtil() {
    }

    public static LocalDateTime parse(String dateTime) {

        try {
            return LocalDateTime.parse(dateTime, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException("Invalid date format. Expected dd-MM-yyyy HH:mm", e);
        }
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static boolean isPast(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }
}