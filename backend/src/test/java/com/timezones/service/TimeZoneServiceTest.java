package com.timezones.service;

import com.timezones.api.repository.TimeZoneRepository;
import com.timezones.domain.exceptions.TimeZoneDateTimeException;
import com.timezones.domain.exceptions.TimeZoneLabelException;
import com.timezones.domain.exceptions.TimeZoneNotFoundException;
import com.timezones.domain.exceptions.TimeZoneOffsetException;
import com.timezones.domain.model.TimeZone;
import com.timezones.domain.service.TimeZoneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        // Given
        when(timeZoneRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<TimeZone> result = timeZoneService.getAll();

        // Then
        verify(timeZoneRepository, times(1)).findAll();
        assertEquals(0, result.size());
    }

    @Test
    public void whenThereAreSomeTimeZones_thenReturnAllTimeZones() {
        // Given
        List<TimeZone> timeZones = List.of(new TimeZone(), new TimeZone());
        when(timeZoneRepository.findAll()).thenReturn(timeZones);

        // When
        List<TimeZone> result = timeZoneService.getAll();

        // Then
        verify(timeZoneRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals(timeZones, result);
    }

    @Test
    public void whenSpecificTimeZoneDoesNotExist_thenThrow() {
        // Given
        Long id = 1L;
        when(timeZoneRepository.findById(id)).thenReturn(Optional.empty());

        try {
            // When
            timeZoneService.getById(id);
        } catch (Exception e) {
            // Then
            verify(timeZoneRepository, times(1)).findById(id);
            assertInstanceOf(TimeZoneNotFoundException.class, e);
        }
    }

    @Test
    public void whenSpecificTimeZoneExists_thenReturn() throws TimeZoneNotFoundException {
        // Given
        Long id = 1L;
        TimeZone timeZone = new TimeZone();
        timeZone.setId(id);
        when(timeZoneRepository.findById(id)).thenReturn(Optional.of(timeZone));

        // When
        TimeZone result = timeZoneService.getById(id);

        // Then
        verify(timeZoneRepository, times(1)).findById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void whenCreateTimeZoneWithNullLabel_thenThrow() {
        // Given
        TimeZone timeZone = new TimeZone();

        try {
            // When
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithEmptyLabel_thenThrow() {
        // Given
        TimeZone timeZone = new TimeZone("", LocalDateTime.now(), ZoneOffset.UTC);

        try {
            // When
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithNullDateTime_thenThrow() {
        // Given
        TimeZone timeZone = new TimeZone("label", null, ZoneOffset.UTC);

        try {
            // When
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneDateTimeException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithNullOffset_thenThrow() {
        // Given
        TimeZone timeZone = new TimeZone("label", LocalDateTime.now(), null);

        try {
            // When
            timeZoneService.create(timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneOffsetException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenCreateTimeZoneWithValidFields_thenSucceed() {
        // Given
        ArgumentCaptor<TimeZone> timeZoneCaptor = ArgumentCaptor.forClass(TimeZone.class);
        TimeZone timeZone = new TimeZone(
                "label",
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                ZoneOffset.UTC
        );
        when(timeZoneRepository.save(timeZone)).thenReturn(timeZone);

        // When
        timeZoneService.create(timeZone);

        // Then
        verify(timeZoneRepository, times(1)).save(timeZoneCaptor.capture());
        TimeZone capturedTimeZone = timeZoneCaptor.getValue();

        assertEquals("label", capturedTimeZone.getLabel());
        assertEquals(LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0), capturedTimeZone.getDateTime());
        assertEquals(ZoneOffset.UTC, capturedTimeZone.getOffsetFromUTC());
    }

    @Test
    public void whenUpdateNonExistingTimeZone_thenThrow() {
        // Given
        Long id = 1L;
        TimeZone timeZone = new TimeZone(
                "label",
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                ZoneOffset.UTC
        );
        when(timeZoneRepository.findById(id)).thenReturn(Optional.empty());

        try {
            // When
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            // Then
            verify(timeZoneRepository, times(1)).findById(id);
            assertInstanceOf(TimeZoneNotFoundException.class, e);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithNullLabel_thenThrow() {
        // Given
        Long id = 1L;
        TimeZone timeZone = new TimeZone(null, LocalDateTime.now(), ZoneOffset.UTC);

        try {
            // When
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithEmptyLabel_thenThrow() {
        // Given
        Long id = 1L;
        TimeZone timeZone = new TimeZone("", LocalDateTime.now(), ZoneOffset.UTC);

        try {
            // When
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneLabelException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithNullDateTime_thenThrow() {
        // Given
        Long id = 1L;
        TimeZone timeZone = new TimeZone("label", null, ZoneOffset.UTC);

        try {
            // When
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneDateTimeException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithNullOffset_thenThrow() {
        // Given
        Long id = 1L;
        TimeZone timeZone = new TimeZone("label", LocalDateTime.now(), null);

        try {
            // When
            timeZoneService.update(id, timeZone);
        } catch (Exception e) {
            // Then
            assertInstanceOf(TimeZoneOffsetException.class, e);
            verify(timeZoneRepository, times(0)).findById(id);
            verify(timeZoneRepository, times(0)).save(timeZone);
        }
    }

    @Test
    public void whenUpdateTimeZoneWithValidFields_thenSucceed() throws TimeZoneNotFoundException {
        // Given
        ArgumentCaptor<TimeZone> timeZoneCaptor = ArgumentCaptor.forClass(TimeZone.class);
        Long id = 1L;
        TimeZone timeZone = new TimeZone(
                "label",
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                ZoneOffset.UTC,
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                null
        );
        when(timeZoneRepository.findById(id)).thenReturn(Optional.of(timeZone));
        when(timeZoneRepository.save(timeZone)).thenReturn(timeZone);

        // When
        timeZoneService.update(id, timeZone);

        // Then
        verify(timeZoneRepository, times(1)).findById(id);
        verify(timeZoneRepository, times(1)).save(timeZoneCaptor.capture());
        TimeZone capturedTimeZone = timeZoneCaptor.getValue();

        assertEquals("label", capturedTimeZone.getLabel());
        assertEquals(LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0), capturedTimeZone.getDateTime());
        assertEquals(ZoneOffset.UTC, capturedTimeZone.getOffsetFromUTC());
        assertEquals(timeZone.getDateTime(), capturedTimeZone.getCreatedAt());
        assertNotEquals(timeZone.getUpdatedAt(), capturedTimeZone.getUpdatedAt());
    }

    @Test
    public void whenDeleteNonExistingTimeZone_thenThrow() {
        // Given
        Long id = 1L;
        when(timeZoneRepository.findById(id)).thenReturn(Optional.empty());

        try {
            // When
            timeZoneService.delete(id);
        } catch (Exception e) {
            // Then
            verify(timeZoneRepository, times(1)).findById(id);
            assertInstanceOf(TimeZoneNotFoundException.class, e);
            verify(timeZoneRepository, times(0)).deleteById(id);
        }
    }

    @Test
    public void whenDeleteExistingTimeZone_thenSucceed() throws TimeZoneNotFoundException {
        // Given
        Long id = 1L;
        TimeZone timeZone = new TimeZone();
        when(timeZoneRepository.findById(id)).thenReturn(Optional.of(timeZone));

        // When
        timeZoneService.delete(id);

        // Then
        verify(timeZoneRepository, times(1)).findById(id);
        verify(timeZoneRepository, times(1)).deleteById(id);
    }
}