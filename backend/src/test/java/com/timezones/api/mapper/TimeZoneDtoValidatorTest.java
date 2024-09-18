package com.timezones.api.mapper;

import com.timezones.api.dto.TimeZoneDto;
import com.timezones.domain.exceptions.TimeZoneDateTimeException;
import com.timezones.domain.exceptions.TimeZoneDateTimeFormatException;
import com.timezones.domain.exceptions.TimeZoneLabelException;
import com.timezones.domain.exceptions.TimeZoneOffsetFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeZoneDtoValidatorTest {

    @Test
    public void whenTimeZoneDtoHasEmptyLabel_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("", "2024-01-01", "+01:00");

        assertThrows(TimeZoneLabelException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasNullLabel_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto(null, "2024-01-01T00:00:00", "-04:00");

        assertThrows(TimeZoneLabelException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasNullDateTime_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("label", null, "-04:00");

        assertThrows(TimeZoneDateTimeException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasInvalidDateTimeFormat_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("label", "2024-01-01", "+02:00");

        assertThrows(TimeZoneDateTimeFormatException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasInvalidOffsetFormat_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("label", "2024-01-01T01:02:03", "02:00");

        assertThrows(TimeZoneOffsetFormatException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoIsValid_thenNotThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("label", "2024-01-01T00:00:00", "+02:00");

        assertDoesNotThrow(() -> TimeZoneDtoValidator.validate(timeZoneDto));
    }
}
