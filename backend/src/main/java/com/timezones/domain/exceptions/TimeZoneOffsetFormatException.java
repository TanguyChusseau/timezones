package com.timezones.domain.exceptions;

public class TimeZoneOffsetFormatException extends IllegalArgumentException {

    public TimeZoneOffsetFormatException() {
        super("Time zone offset should have this specific format: +HH:mm or -HH:mm");
    }
}