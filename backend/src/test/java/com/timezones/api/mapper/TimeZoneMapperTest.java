package com.timezones.api.mapper;

import com.timezones.api.dto.PartialTimeZoneDto;
import com.timezones.api.dto.TimeZoneDto;
import com.timezones.domain.model.TimeZone;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeZoneMapperTest {

    @Test
    public void whenTimeZone_thenReturnTimeZoneDto() {
        // Given
        TimeZone timeZone = new TimeZone(
                "label",
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0),
                ZoneOffset.UTC
        );
        timeZone.setId(1L);
        timeZone.setCreatedAt(LocalDateTime.of(2024, Month.FEBRUARY, 1, 15, 46, 7));
        timeZone.setUpdatedAt(LocalDateTime.of(2024, Month.MARCH, 1, 6, 21, 38));

        // When
        TimeZoneDto timeZoneDto = TimeZoneMapper.toDto(timeZone);

        // Then
        assertEquals(1L, timeZoneDto.getId());
        assertEquals("label", timeZoneDto.getLabel());
        assertEquals("2024-01-01T00:00:00", timeZoneDto.getDateTime());
        assertEquals("+00:00", timeZoneDto.getOffsetFromUTC());
        assertEquals("2024-02-01T15:46:07", timeZoneDto.getCreatedAt());
        assertEquals("2024-03-01T06:21:38", timeZoneDto.getUpdatedAt());
    }

    @Test
    public void whenTimeZoneDto_thenReturnTimeZone() {
        // Given
        PartialTimeZoneDto partialTimeZoneDto = new PartialTimeZoneDto("label", "2024-04-01T02:00:00", "-05:30");

        // When
        TimeZone timeZone = TimeZoneMapper.toEntity(partialTimeZoneDto);

        // Then
        assertEquals("label", timeZone.getLabel());
        assertEquals(
                LocalDateTime.of(2024, Month.APRIL, 1, 2, 0, 0),
                timeZone.getDateTime()
        );
        assertEquals(ZoneOffset.ofHoursMinutes(-5, -30), timeZone.getOffsetFromUTC());
    }
}
