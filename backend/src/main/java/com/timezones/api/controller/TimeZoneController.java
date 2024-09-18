package com.timezones.api.controller;

import com.timezones.api.dto.PartialTimeZoneDto;
import com.timezones.api.dto.TimeZoneDto;
import com.timezones.api.mapper.TimeZoneMapper;
import com.timezones.domain.model.TimeZone;
import com.timezones.domain.service.TimeZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all time zones", tags = { "Get all" })
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeZoneDto.class)))
    )
    @GetMapping
    public List<TimeZoneDto> getAllTimeZones() {
        return timeZoneService.getAll().stream()
                .map(TimeZoneMapper::toDto)
                .toList();
    }

    @Operation(summary = "Get a time zone by it's id", tags = { "Get one" })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = TimeZoneDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Time zone not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            TimeZone timeZone = this.timeZoneService.getById(id);
            return new ResponseEntity<>(TimeZoneMapper.toDto(timeZone), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @Operation(
            summary = "Create a new time zone",
            tags = { "Create one" },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The time zone to create",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartialTimeZoneDto.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Time zone created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Time zone not found")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody PartialTimeZoneDto partialTimeZoneDto) {
        try {
            this.timeZoneService.create(TimeZoneMapper.toEntity(partialTimeZoneDto));
            return new ResponseEntity<>("Time zone created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Update a time zone by it's id",
            tags = { "Update one" },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The time zone to update",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartialTimeZoneDto.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Time zone updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Time zone not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody PartialTimeZoneDto partialTimeZoneDto) {
        try {
            timeZoneService.update(id, TimeZoneMapper.toEntity(partialTimeZoneDto));
            return new ResponseEntity<>("Time zone updated successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a time zone by it's id", tags = { "Delete one" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Time zone deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Time zone not found")
    })
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