package com.timezones.domain.exceptions;

public class TimeZoneNotFoundException extends Exception {

    public TimeZoneNotFoundException(Long id) {
        super("Time zone with id: " + id + " not found");
    }
}