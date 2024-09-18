package com.timezones.api.mapper;

import com.timezones.api.dto.PartialTimeZoneDto;
import com.timezones.domain.exceptions.*;

import java.util.regex.Pattern;

public class TimeZoneDtoValidator {
    private static final String DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$";
    private static final String OFFSET_REGEX = "^[+-]\\d{2}:\\d{2}$";

    public static void validate(PartialTimeZoneDto partialTimeZoneDto) throws IllegalArgumentException {
        if (partialTimeZoneDto.getLabel() == null || partialTimeZoneDto.getLabel().isEmpty()) {
            throw new TimeZoneLabelException();
        }

        if (partialTimeZoneDto.getDateTime() == null || partialTimeZoneDto.getDateTime().isEmpty()) {
            throw new TimeZoneDateTimeException();
        }

        if (partialTimeZoneDto.getOffsetFromUTC() == null || partialTimeZoneDto.getOffsetFromUTC().isEmpty()) {
            throw new TimeZoneOffsetException();
        }

        if (!Pattern.matches(DATE_TIME_REGEX, partialTimeZoneDto.getDateTime())) {
            throw new TimeZoneDateTimeFormatException();
        }

        if (!Pattern.matches(OFFSET_REGEX, partialTimeZoneDto.getOffsetFromUTC())) {
            throw new TimeZoneOffsetFormatException();
        }
    }
}
