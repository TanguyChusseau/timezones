package com.tanguy.ch.timezones.api.mapper;

import com.tanguy.ch.timezones.api.dto.TimeZoneDto;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneDateTimeException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneDateTimeFormatException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneLabelException;

import java.util.regex.Pattern;

public class TimeZoneValidator {
    private static final String OFFSET_DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}:\\d{2}$";

    public static void validateTimeZone(TimeZoneDto timeZoneDto) throws IllegalArgumentException {
        if (timeZoneDto.getLabel() == null || timeZoneDto.getLabel().isEmpty()) {
            throw new TimeZoneLabelException();
        }

        if (timeZoneDto.getDateTime() == null) {
            throw new TimeZoneDateTimeException();
        }

        if (!Pattern.matches(OFFSET_DATE_TIME_REGEX, timeZoneDto.getDateTime())) {
            throw new TimeZoneDateTimeFormatException();
        }
    }
}
