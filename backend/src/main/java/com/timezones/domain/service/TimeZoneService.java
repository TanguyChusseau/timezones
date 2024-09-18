package com.timezones.domain.service;

import com.timezones.api.repository.TimeZoneRepository;
import com.timezones.domain.exceptions.TimeZoneDateTimeException;
import com.timezones.domain.exceptions.TimeZoneLabelException;
import com.timezones.domain.exceptions.TimeZoneNotFoundException;
import com.timezones.domain.exceptions.TimeZoneOffsetException;
import com.timezones.domain.model.TimeZone;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeZoneService {

    private final TimeZoneRepository timeZoneRepository;

    public TimeZoneService(TimeZoneRepository timeZoneRepository) {
        this.timeZoneRepository = timeZoneRepository;
    }

    public List<TimeZone> getAll() {
        return timeZoneRepository.findAll();
    }

    public TimeZone getById(Long id) throws TimeZoneNotFoundException {
        return timeZoneRepository.findById(id).orElseThrow(() -> new TimeZoneNotFoundException(id));
    }

    public void create(TimeZone timeZone) {
        this.validate(timeZone);
        LocalDateTime now = LocalDateTime.now();
        timeZone.setCreatedAt(now);
        timeZone.setUpdatedAt(now);
        timeZoneRepository.save(timeZone);
    }

    public void update(Long id, TimeZone timeZone) throws TimeZoneNotFoundException {
        this.validate(timeZone);
        TimeZone timeZoneFromDb = timeZoneRepository.findById(id).orElseThrow(() -> new TimeZoneNotFoundException(id));
        timeZoneFromDb.setLabel(timeZone.getLabel());
        timeZoneFromDb.setDateTime(timeZone.getDateTime());
        timeZoneFromDb.setOffsetFromUTC(timeZone.getOffsetFromUTC());
        timeZoneFromDb.setUpdatedAt(LocalDateTime.now());
        timeZoneFromDb.setCreatedAt(timeZone.getCreatedAt());
        timeZoneRepository.save(timeZone);
    }

    public void delete(Long id) throws TimeZoneNotFoundException {
        timeZoneRepository.findById(id).orElseThrow(() -> new TimeZoneNotFoundException(id));
        timeZoneRepository.deleteById(id);
    }

    private void validate(TimeZone timeZone) {
        if (timeZone.getLabel() == null || timeZone.getLabel().isEmpty()) {
            throw new TimeZoneLabelException();
        }

        if (timeZone.getDateTime() == null) {
            throw new TimeZoneDateTimeException();
        }

        if (timeZone.getOffsetFromUTC() == null) {
            throw new TimeZoneOffsetException();
        }
    }
}