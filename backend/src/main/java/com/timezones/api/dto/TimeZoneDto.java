package com.timezones.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeZoneDto {
    private Long id;
    private String label;
    private String dateTime;
    private String offsetFromUTC;
    private String createdAt;
    private String updatedAt;

    public TimeZoneDto(String label, String dateTime, String offsetFromUTC) {
        this.label = label;
        this.dateTime = dateTime;
        this.offsetFromUTC = offsetFromUTC;
    }
}