package com.tanguy.ch.timezones.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@Entity
public class TimeZone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;  // Ex: "Europe/Paris"

    private LocalDateTime dateTime;  // Ex: "15:00:00"

    private ZoneOffset offsetFromUTC;  // Ex: "-2"
}