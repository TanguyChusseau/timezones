package com.tanguy.ch.timezones.domain.service;

import com.tanguy.ch.timezones.api.repository.TimeZoneRepository;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneDateTimeException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneLabelException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneNotFoundException;
import com.tanguy.ch.timezones.domain.exceptions.TimeZoneOffsetException;
import com.tanguy.ch.timezones.domain.model.TimeZone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeZoneServiceTest {

    @Mock
    private TimeZoneRepository timeZoneRepository;

    @InjectMocks
    private TimeZoneService timeZoneService;

    @Test
    public void whenThereAreNoTimeZones_thenReturnEmptyArray() {
        when(timeZoneRepository.findAll()).thenReturn(new ArrayList<>());

        List<TimeZone> result = timeZoneService.getAll();

        verify(timeZoneRepository, times(1)).findAll();
        assertEquals(0, result.size());
    }

    @Test
    public void whenThereAreSomeTimeZones_thenReturnAllTimeZones() {
        TimeZone parisTimeZone = new TimeZone();
        TimeZone tokyoTimeZone = new TimeZone();
        List<TimeZone> timeZones = List.of(parisTimeZone, tokyoTimeZone);

        when(timeZoneRepository.findAll()).thenReturn(timeZones);

        List<TimeZone> result = timeZoneService.getAll();

        verify(timeZoneRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals(timeZones, result);
    }

    @Test
    public void whenSpecificTimeZoneDoesNotExist_thenThrow() {
        Long id = 1L;
        when(timeZoneRepository.findById(id)).thenReturn(Optional.empty());

        try {
            timeZoneService.getById(id);
        } catch (Exception e) {
            verify(timeZoneRepository, times(1)).findById(id);
            assertInstanceOf(TimeZoneNotFoundException.class, e);
        }
    }

    @Test
    public void whenSpecificTimeZoneExists_thenReturn() throws TimeZoneNotFoundException {
        Long id = 1L;
        TimeZone timeZone = new TimeZone();
        timeZone.setId(id);
        when(timeZoneRepository.findById(id)).thenReturn(Optional.of(timeZone));

        TimeZone result = timeZoneService.getById(id);

        verify(timeZoneRepository, times(1)).findById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void whenCreateTimeZoneWithNullLabel_thenThrow() {
        TimeZone timeZone = new TimeZone();

        try {
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithEmptyLabel_thenThrow() {
        TimeZone timeZone = new TimeZone("", LocalDateTime.now(), ZoneOffset.UTC);

        try {
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithNullDateTime_thenThrow() {
        TimeZone timeZone = new TimeZone("label", null, ZoneOffset.UTC);

        try {
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneDateTimeException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithNullOffset_thenThrow() {
        TimeZone timeZone = new TimeZone("label", LocalDateTime.now(), null);

        try {
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneOffsetException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithValidFields_thenSucceed() {
        TimeZone timeZone = new TimeZone(
                "label",
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                ZoneOffset.UTC
        );
        when(timeZoneRepository.save(timeZone)).thenReturn(timeZone);

        timeZoneService.create(timeZone);

        verify(timeZoneRepository, times(1)).save(timeZone);
    }

    @Test
    public void whenUpdateNonExistingTimeZone_thenThrow() {
        Long id = 1L;
        TimeZone timeZone = new TimeZone(
                "label",
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                ZoneOffset.UTC
        );
        when(timeZoneRepository.findById(id)).thenReturn(Optional.empty());

        try {
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            verify(timeZoneRepository, times(1)).findById(id);
            assertInstanceOf(TimeZoneNotFoundException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithNullLabel_thenThrow() {
        Long id = 1L;
        TimeZone timeZone = new TimeZone(null, LocalDateTime.now(), ZoneOffset.UTC);

        try {
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithEmptyLabel_thenThrow() {
        Long id = 1L;
        TimeZone timeZone = new TimeZone("", LocalDateTime.now(), ZoneOffset.UTC);

        try {
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithNullDateTime_thenThrow() {
        Long id = 1L;
        TimeZone timeZone = new TimeZone("label", null, ZoneOffset.UTC);

        try {
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneDateTimeException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithNullOffset_thenThrow() {
        Long id = 1L;
        TimeZone timeZone = new TimeZone("label", LocalDateTime.now(), null);

        try {
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            assertInstanceOf(TimeZoneOffsetException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithValidFields_thenSucceed() throws TimeZoneNotFoundException {
        Long id = 1L;
        TimeZone timeZone = new TimeZone(
                "label",
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                ZoneOffset.UTC
        );
        when(timeZoneRepository.findById(id)).thenReturn(Optional.of(timeZone));
        when(timeZoneRepository.save(timeZone)).thenReturn(timeZone);

        timeZoneService.update(id, timeZone);

        verify(timeZoneRepository, times(1)).findById(id);
        verify(timeZoneRepository, times(1)).save(timeZone);
    }

    @Test
    public void whenDeleteNonExistingTimeZone_thenThrow() {
        Long id = 1L;
        when(timeZoneRepository.findById(id)).thenReturn(Optional.empty());

        try {
            timeZoneService.delete(id);
        } catch (Exception e) {
            verify(timeZoneRepository, times(1)).findById(id);
            assertInstanceOf(TimeZoneNotFoundException.class, e);
            verify(timeZoneRepository, times(0)).deleteById(id);
        }
    }

    @Test
    public void whenDeleteExistingTimeZone_thenSucceed() throws TimeZoneNotFoundException {
        Long id = 1L;
        TimeZone timeZone = new TimeZone();
        when(timeZoneRepository.findById(id)).thenReturn(Optional.of(timeZone));

        timeZoneService.delete(id);

        verify(timeZoneRepository, times(1)).findById(id);
        verify(timeZoneRepository, times(1)).deleteById(id);
    }
}
