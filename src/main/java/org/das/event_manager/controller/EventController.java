package org.das.event_manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Event;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.das.event_manager.dto.mappers.EventDtoMapper;
import org.das.event_manager.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;

    public EventController(
            EventService eventService,
            EventDtoMapper eventDtoMapper
    ) {
        this.eventService = eventService;
        this.eventDtoMapper = eventDtoMapper;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> create(
            @Valid @RequestBody EventCreateRequestDto eventCreateRequestDto
    ) {
        LOGGER.info("Post request for create EventCreateRequestDto {}", eventCreateRequestDto);
        Event event = eventDtoMapper.toDomain(eventCreateRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventDtoMapper.toDto(eventService.create(event)));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteById(
            @NotNull @PathVariable("eventId") Long eventId
    ) {
        eventService.deleteById(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> findById(
            @NotNull @PathVariable("eventId") Long eventId
    ) {
        return ResponseEntity.ok().body(eventDtoMapper.toDto(eventService.findById(eventId)));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateById(
            @NotNull @PathVariable("eventId") Long eventId,
            @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto
            ) {
        Event eventToUpdate = eventDtoMapper.toDomain(eventUpdateRequestDto);
        return ResponseEntity.ok().body(eventDtoMapper.toDto(eventService.update(eventId, eventToUpdate)));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventResponseDto>> findAllEvents(
            @RequestBody EventSearchRequestDto eventSearchRequestDto
            ) {
        return ResponseEntity.ok().body(eventDtoMapper.toDto(eventService.search(eventSearchRequestDto)));
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> findEventsByUserCreation() {
        return ResponseEntity.ok().body(eventDtoMapper.toDto(eventService.findAllEventsCreationByOwner()));
    }

    @PostMapping("/registrations/{eventId}")
    public ResponseEntity<Void> registrationUserOnEvent(
            @NotNull @PathVariable("eventId") Long eventId
    ) {
        eventService.registrationOnEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/registrations/cancel/{eventId}")
    public ResponseEntity<Void> registrationUserCancelEvent(
            @NotNull @PathVariable("eventId") Long eventId
    ) {
        eventService.registrationCancelByEventId(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/registrations/my")
    public ResponseEntity<List<EventResponseDto>> findAllEventsByUserRegistration() {
        return ResponseEntity.ok()
                .body(eventDtoMapper.toDto(eventService.findAllEventByUserRegistration()));
    }
}
