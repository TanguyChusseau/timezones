package com.tanguy.ch.timezones.api.dto;

import lombok.Data;

@Data
public class TimeZoneDto {
    private String label;
    private String dateTime;
    private String offsetFromUTC;
}