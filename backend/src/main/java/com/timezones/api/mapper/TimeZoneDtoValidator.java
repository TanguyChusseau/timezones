package com.timezones.api.mapper;

import com.timezones.api.dto.TimeZoneDto;
import com.timezones.domain.exceptions.*;

import java.util.regex.Pattern;

public class TimeZoneDtoValidator {
    private static final String DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$";
    private static final String OFFSET_REGEX = "^[+-]\\d{2}:\\d{2}$";

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

        if (!Pattern.matches(DATE_TIME_REGEX, timeZoneDto.getDateTime())) {
            throw new TimeZoneDateTimeFormatException();
        }

        if (!Pattern.matches(OFFSET_REGEX, timeZoneDto.getOffsetFromUTC())) {
            throw new TimeZoneOffsetFormatException();
        }
    }
}
