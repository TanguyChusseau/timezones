package com.tanguy.ch.timezones.api.mapper;

import com.tanguy.ch.timezones.api.dto.TimeZoneDto;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneDateTimeException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneDateTimeFormatException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneLabelException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeZoneValidatorTest {

    @Test
    public void whenTimeZoneHasEmptyLabel_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("", "2024-01-01", "+01:00");

        assertThrows(TimeZoneLabelException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneHasNullLabel_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto(null, "2024-01-01T00:00:00", "-04:00");

        assertThrows(TimeZoneLabelException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneHasNullDateTime_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("label", null, "-04:00");

        assertThrows(TimeZoneDateTimeException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneHasInvalidDateTimeFormat_thenThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("label", "2024-01-01", "+02:00");

        assertThrows(TimeZoneDateTimeFormatException.class, () -> {
            TimeZoneDtoValidator.validate(timeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneIsValid_thenNotThrow() {
        TimeZoneDto timeZoneDto = new TimeZoneDto("label", "2024-01-01T00:00:00", "+02:00");

        assertDoesNotThrow(() -> TimeZoneDtoValidator.validate(timeZoneDto));
    }
}
