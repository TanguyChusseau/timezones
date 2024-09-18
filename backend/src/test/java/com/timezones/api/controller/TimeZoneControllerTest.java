package com.timezones.api.controller;

import com.timezones.api.repository.TimeZoneRepository;
import com.timezones.domain.model.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @BeforeEach
    public void setup() {
        timeZoneRepository.deleteAll();
    }

    // Get all time zones
    @Test
    public void whenRetrieveAllTimeZonesFromEmptyDatabase_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void whenRetrieveAllTimeZonesFromNonEmptyDatabase_thenReturnAllTimeZones() throws Exception {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 3, 1, 2, 3);
        timeZoneRepository.save(new TimeZone("label", dateTime, ZoneOffset.UTC, dateTime, dateTime));
        timeZoneRepository.save(new TimeZone("otherLabel", dateTime, ZoneOffset.ofHoursMinutes(4, 25), dateTime, dateTime));

        // When and Then
        mockMvc.perform(get("/api/timezones").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"label\":\"label\",\"dateTime\":\"2024-03-03T01:02:03\",\"offsetFromUTC\":\"+00:00\",\"createdAt\":\"2024-03-03T01:02:03\",\"updatedAt\":\"2024-03-03T01:02:03\"}," +
                        "{\"label\":\"otherLabel\",\"dateTime\":\"2024-03-03T01:02:03\",\"offsetFromUTC\":\"+04:25\",\"createdAt\":\"2024-03-03T01:02:03\",\"updatedAt\":\"2024-03-03T01:02:03\"}]"
                , false));
    }

    // Get a time zone
    @Test
    public void whenRetrieveNonExistingTimeZone_thenReturnNotFound() throws Exception {
        // When and Then
        mockMvc.perform(get("/api/timezones/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Time zone with id: 999 not found"))
        ;
    }

    @Test
    public void whenRetrieveExistingTimeZone_thenReturnTimeZone() throws Exception {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(2024, 4, 3, 1, 2, 3);
        TimeZone savedTimeZone =
                timeZoneRepository.save(new TimeZone("label", dateTime, ZoneOffset.UTC, dateTime, dateTime));

        // When and Then
        mockMvc.perform(get("/api/timezones/" + savedTimeZone.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("label"))
                .andExpect(jsonPath("$.dateTime").value("2024-04-03T01:02:03"))
                .andExpect(jsonPath("$.offsetFromUTC").value("+00:00"))
                .andExpect(jsonPath("$.createdAt").value("2024-04-03T01:02:03"))
                .andExpect(jsonPath("$.updatedAt").value("2024-04-03T01:02:03"));
    }

    // Create a time zone
    @Test
    public void whenCreateTimeZoneWithNullLabel_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": null, \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone label should be a non-empty string"));
    }

    @Test
    public void whenCreateTimeZoneWithEmptyLabel_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone label should be a non-empty string"));
    }

    @Test
    public void whenCreateTimeZoneWithNullDateTime_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"UTC\", \"dateTime\": null, \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone date-time should be a non-empty string"));
    }

    @Test
    public void whenCreateTimeZoneWithEmptyDateTime_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"UTC\", \"dateTime\": \"\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone date-time should be a non-empty string"));
    }

    @Test
    public void whenCreateTimeZoneWithNullOffset_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": null}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone offset should be a non-empty string"));
    }

    @Test
    public void whenCreateTimeZoneWithEmptyOffset_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone offset should be a non-empty string"));
    }

    @Test
    public void whenCreateTimeZoneWithInvalidDateTimeFormat_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01T12:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone date-time should have an ISO 8601 format, for example: 2024-01-01T00:00:00"));
    }

    @Test
    public void whenCreateTimeZoneWithInvalidOffsetFormat_thenReturnBadRequest() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"00\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone offset should have this specific format: +HH:mm or -HH:mm"));
    }
    
    @Test
    public void whenCreateValidTimeZone_thenReturnCreated() throws Exception {
        // Given
        String timeZoneToCreate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(post("/api/timezones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToCreate))
                .andExpect(status().isCreated())
                .andExpect(content().string("Time zone created successfully"));
    }

    // Update a time zone
    @Test
    public void whenUpdateNonExistingTimeZone_thenReturnNotFound() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"label\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"+00:00\"}";
        long wrongTimeZoneId = timeZoneFromDbId + 1L;

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + wrongTimeZoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Time zone with id: " + wrongTimeZoneId + " not found"));
    }

    @Test
    public void whenUpdateTimeZoneWithNullLabel_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": null, \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone label should be a non-empty string"));
    }

    @Test
    public void whenUpdateTimeZoneWithEmptyLabel_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone label should be a non-empty string"));
    }

    @Test
    public void whenUpdateTimeZoneWithNullDateTime_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"UTC\", \"dateTime\": null, \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone date-time should be a non-empty string"));
    }

    @Test
    public void whenUpdateTimeZoneWithEmptyDateTime_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"UTC\", \"dateTime\": \"\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone date-time should be a non-empty string"));
    }

    @Test
    public void whenUpdateTimeZoneWithNullOffset_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": null}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone offset should be a non-empty string"));
    }

    @Test
    public void whenUpdateTimeZoneWithEmptyOffset_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone offset should be a non-empty string"));
    }

    @Test
    public void whenUpdateTimeZoneWithInvalidDateTimeFormat_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01T12:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone date-time should have an ISO 8601 format, for example: 2024-01-01T00:00:00"));
    }

    @Test
    public void whenUpdateTimeZoneWithInvalidOffsetFormat_thenReturnBadRequest() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"00\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time zone offset should have this specific format: +HH:mm or -HH:mm"));
    }

    @Test
    public void whenUpdateValidTimeZone_thenReturnNoContent() throws Exception {
        // Given
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();
        String timeZoneToUpdate = "{\"label\": \"UTC\", \"dateTime\": \"2024-01-01T12:00:00\", \"offsetFromUTC\": \"+00:00\"}";

        // When and Then
        mockMvc.perform(patch("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeZoneToUpdate))
                .andExpect(status().isNoContent());
    }

    // Delete a time zone
    @Test
    public void whenDeleteNonExistingTimeZone_thenReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/timezones/" + 123123L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Time zone with id: 123123 not found"));
    }

    @Test
    public void whenDeleteExistingTimeZone_thenReturnNoContent() throws Exception {
        long timeZoneFromDbId = this.setupTimeZoneToUpdate();

        mockMvc.perform(delete("/api/timezones/" + timeZoneFromDbId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<TimeZone> deletedTimeZone = timeZoneRepository.findById(timeZoneFromDbId);
        assert deletedTimeZone.isEmpty();
    }

    private Long setupTimeZoneToUpdate() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 4, 3, 1, 2, 3);
        TimeZone timeZoneFromDb =
                timeZoneRepository.save(new TimeZone("label", dateTime, ZoneOffset.UTC, dateTime, dateTime));
        return timeZoneFromDb.getId();
    }
}
