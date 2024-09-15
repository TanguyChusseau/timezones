package com.tanguy.ch.timezones.domain.exceptions;

public class TimeZoneDateTimeFormatException extends IllegalArgumentException {

    public TimeZoneDateTimeFormatException() {
        super("Time zone date-time should have an ISO 8601 format, for example: 2024-01-01T00:00:00+02:00");
    }
}