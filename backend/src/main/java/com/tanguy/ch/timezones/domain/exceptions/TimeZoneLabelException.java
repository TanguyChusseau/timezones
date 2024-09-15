package com.tanguy.ch.timezones.domain.exceptions;

public class TimeZoneLabelException extends IllegalArgumentException {

    public TimeZoneLabelException() {
        super("Time zone label should be a non-empty string");
    }
}