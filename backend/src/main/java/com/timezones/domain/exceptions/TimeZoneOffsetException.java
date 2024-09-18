package com.timezones.domain.exceptions;

public class TimeZoneOffsetException extends IllegalArgumentException {

    public TimeZoneOffsetException() {
        super("Time zone offset should be a non-empty string");
    }
}