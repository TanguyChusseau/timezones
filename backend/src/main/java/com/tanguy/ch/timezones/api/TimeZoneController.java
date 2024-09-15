package com.tanguy.ch.timezones.api;

import com.tanguy.ch.timezones.api.dto.TimeZoneDto;
import com.tanguy.ch.timezones.api.mapper.TimeZoneMapper;
import com.tanguy.ch.timezones.domain.model.TimeZone;
import com.tanguy.ch.timezones.domain.service.TimeZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timezones")
public class TimeZoneController {

    private final TimeZoneService timeZoneService;

    public TimeZoneController(TimeZoneService timeZoneService) {
        this.timeZoneService = timeZoneService;
    }

    @GetMapping
    public List<TimeZoneDto> getAllTimeZones() {
        return timeZoneService.getAll().stream()
                .map(TimeZoneMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            TimeZone timeZone = this.timeZoneService.getById(id);
            return new ResponseEntity<>(TimeZoneMapper.toDto(timeZone), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody TimeZoneDto timeZoneDto) {
        try {
            this.timeZoneService.create(TimeZoneMapper.toEntity(timeZoneDto));
            return new ResponseEntity<>("Time zone created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody TimeZoneDto timeZoneDto) {
        try {
            timeZoneService.update(id, TimeZoneMapper.toEntity(timeZoneDto));
            return new ResponseEntity<>("Time zone updated successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            timeZoneService.delete(id);
            return new ResponseEntity<>("Time zone deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }
}