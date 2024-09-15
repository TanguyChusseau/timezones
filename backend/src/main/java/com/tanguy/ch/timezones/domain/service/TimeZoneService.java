package com.tanguy.ch.timezones.domain.service;

import com.tanguy.ch.timezones.api.repository.TimeZoneRepository;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneNotFoundException;
import com.tanguy.ch.timezones.domain.model.TimeZone;
import org.springframework.stereotype.Service;

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
        timeZoneRepository.save(timeZone);
    }

    public void update(Long id, TimeZone timeZone) throws TimeZoneNotFoundException {
        timeZoneRepository.findById(id).orElseThrow(() -> new TimeZoneNotFoundException(id));
        timeZoneRepository.save(timeZone);
    }

    public void delete(Long id) throws TimeZoneNotFoundException {
        timeZoneRepository.findById(id).orElseThrow(() -> new TimeZoneNotFoundException(id));
        timeZoneRepository.deleteById(id);
    }
}