package com.tanguy.ch.timezones.api.mapper;

import com.tanguy.ch.timezones.api.dto.TimeZoneDto;
import com.tanguy.ch.timezones.domain.model.TimeZone;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeZoneMapper {

    public static TimeZoneDto toDto(TimeZone timeZone) {
        TimeZoneDto dto = new TimeZoneDto();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

        dto.setId(timeZone.getId());
        dto.setLabel(timeZone.getLabel());
        dto.setDateTime(timeZone.getDateTime().format(dateTimeFormatter));
        dto.setOffsetFromUTC(
                timeZone.getOffsetFromUTC() == ZoneOffset.UTC ? "+00:00" : timeZone.getOffsetFromUTC().toString()
        );
        dto.setCreatedAt(timeZone.getCreatedAt().format(dateTimeFormatter));
        dto.setUpdatedAt(timeZone.getUpdatedAt().format(dateTimeFormatter));

        return dto;
    }

    public static TimeZone toEntity(TimeZoneDto timeZoneDto) {
        TimeZoneDtoValidator.validate(timeZoneDto);
        TimeZone timeZone = new TimeZone();

        timeZone.setLabel(timeZoneDto.getLabel());
        timeZone.setDateTime(LocalDateTime.parse(timeZoneDto.getDateTime()));
        timeZone.setOffsetFromUTC(ZoneOffset.of(timeZoneDto.getOffsetFromUTC()));

        return timeZone;
    }
}