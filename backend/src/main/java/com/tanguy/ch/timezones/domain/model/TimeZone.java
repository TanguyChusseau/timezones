package com.tanguy.ch.timezones.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@Entity
public class TimeZone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;  // Ex: "Europe/Paris"

    private LocalDateTime dateTime;  // Ex: "2024-01-01T00:00:00"

    private ZoneOffset offsetFromUTC;  // Ex: "-2"

    private LocalDateTime createdAt;  // Ex: "2024-01-01T00:00:00"

    private LocalDateTime updatedAt;  // Ex: "2024-01-01T00:00:00"

    public TimeZone(String label, LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        this.label = label;
        this.dateTime = localDateTime;
        this.offsetFromUTC = zoneOffset;
    }
}