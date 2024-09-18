package com.timezones.api.mapper;

import com.timezones.api.dto.PartialTimeZoneDto;
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
        PartialTimeZoneDto partialTimeZoneDto = new PartialTimeZoneDto("", "2024-01-01", "+01:00");

        assertThrows(TimeZoneLabelException.class, () -> {
            TimeZoneDtoValidator.validate(partialTimeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasNullLabel_thenThrow() {
        PartialTimeZoneDto partialTimeZoneDto = new PartialTimeZoneDto(null, "2024-01-01T00:00:00", "-04:00");

        assertThrows(TimeZoneLabelException.class, () -> {
            TimeZoneDtoValidator.validate(partialTimeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasNullDateTime_thenThrow() {
        PartialTimeZoneDto partialTimeZoneDto = new PartialTimeZoneDto("label", null, "-04:00");

        assertThrows(TimeZoneDateTimeException.class, () -> {
            TimeZoneDtoValidator.validate(partialTimeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasInvalidDateTimeFormat_thenThrow() {
        PartialTimeZoneDto partialTimeZoneDto = new PartialTimeZoneDto("label", "2024-01-01", "+02:00");

        assertThrows(TimeZoneDateTimeFormatException.class, () -> {
            TimeZoneDtoValidator.validate(partialTimeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoHasInvalidOffsetFormat_thenThrow() {
        PartialTimeZoneDto partialTimeZoneDto = new PartialTimeZoneDto("label", "2024-01-01T01:02:03", "02:00");

        assertThrows(TimeZoneOffsetFormatException.class, () -> {
            TimeZoneDtoValidator.validate(partialTimeZoneDto);
        });
    }

    @Test
    public void whenTimeZoneDtoIsValid_thenNotThrow() {
        PartialTimeZoneDto partialTimeZoneDto = new PartialTimeZoneDto("label", "2024-01-01T00:00:00", "+02:00");

        assertDoesNotThrow(() -> TimeZoneDtoValidator.validate(partialTimeZoneDto));
    }
}
