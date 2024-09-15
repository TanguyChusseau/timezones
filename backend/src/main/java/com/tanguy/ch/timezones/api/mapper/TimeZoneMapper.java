package com.tanguy.ch.timezones.api.mapper;

import com.tanguy.ch.timezones.api.dto.TimeZoneDto;
import com.tanguy.ch.timezones.domain.model.TimeZone;

import java.time.OffsetDateTime;

public class TimeZoneMapper {

    public static TimeZoneDto toDto(TimeZone timeZone) {
        TimeZoneDto dto = new TimeZoneDto();

        dto.setLabel(timeZone.getLabel());
        dto.setDateTime(timeZone.getDateTime().toString());
        dto.setOffsetFromUTC(timeZone.getOffsetFromUTC().toString());

        return dto;
    }

    public static TimeZone toEntity(TimeZoneDto timeZoneDto) {
        TimeZoneValidator.validateTimeZone(timeZoneDto);
        TimeZone timeZone = new TimeZone();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();

        timeZone.setLabel(timeZoneDto.getLabel());
        timeZone.setDateTime(offsetDateTime.toLocalDateTime());
        timeZone.setOffsetFromUTC(offsetDateTime.getOffset());

        return timeZone;
    }
}