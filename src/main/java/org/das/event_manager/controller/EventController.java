package org.das.event_manager.controller;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.User;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.das.event_manager.dto.mappers.EventDtoMapper;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<EventDto> create(@RequestBody EventCreateRequestDto eventCreateRequestDto) {
        LOGGER.info("Post request for create EventCreateRequestDto {}", eventCreateRequestDto);
        Event event = eventDtoMapper.toDomain(eventCreateRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventDtoMapper.toDto(eventService.create(event)));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteById(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> findById(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateById(
            @PathVariable("eventId") Long eventId,
            @RequestBody EventUpdateRequestDto eventUpdateRequestDto
            ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> findEventsByFilter(
            @RequestBody EventUpdateRequestDto eventUpdateRequestDto
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> findEventsByUserCreate() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registrations/{eventId}")
    public ResponseEntity<Void> registrationUserOnEvent(
            @PathVariable("eventId") Long eventId
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/registrations/cancel/{eventId}")
    public ResponseEntity<Void> registrationUserCancelEvent(
            @PathVariable("eventId") Long eventId
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/registrations/my")
    public ResponseEntity<EventDto> findEventsByUserRegistration() {
        return ResponseEntity.ok().build();
    }
}
