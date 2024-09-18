package com.timezones.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartialTimeZoneDto {

    @Schema(example = "label")
    private String label;

    @Schema(example = "2024-03-03T01:02:03")
    private String dateTime;

    @Schema(example = "+01:30")
    private String offsetFromUTC;

    public PartialTimeZoneDto(String label, String dateTime, String offsetFromUTC) {
        this.label = label;
        this.dateTime = dateTime;
        this.offsetFromUTC = offsetFromUTC;
    }
}