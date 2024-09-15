package com.tanguy.ch.timezones.domain.exceptions;

public class TimeZoneDateTimeException extends IllegalArgumentException {

    public TimeZoneDateTimeException() {
        super("Time zone date-time should not be null");
    }
}