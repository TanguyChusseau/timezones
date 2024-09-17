package com.tanguy.ch.timezones.api.mapper;

import com.tanguy.ch.timezones.api.dto.TimeZoneDto;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneDateTimeException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneDateTimeFormatException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneLabelException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneOffsetException;

import java.util.regex.Pattern;

public class TimeZoneDtoValidator {
    private static final String OFFSET_DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}:\\d{2}$";

    public static void validate(TimeZoneDto timeZoneDto) throws IllegalArgumentException {
        if (timeZoneDto.getLabel() == null || timeZoneDto.getLabel().isEmpty()) {
            throw new TimeZoneLabelException();
        }

        if (timeZoneDto.getDateTime() == null || timeZoneDto.getDateTime().isEmpty()) {
            throw new TimeZoneDateTimeException();
        }

        if (timeZoneDto.getOffsetFromUTC() == null || timeZoneDto.getOffsetFromUTC().isEmpty()) {
            throw new TimeZoneOffsetException();
        }

        if (!Pattern.matches(OFFSET_DATE_TIME_REGEX, timeZoneDto.getDateTime() + timeZoneDto.getOffsetFromUTC())) {
            throw new TimeZoneDateTimeFormatException();
        }
    }
}
