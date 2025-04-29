package org.das.event_manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.Event;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;
    private final EventMapper eventMapper;


    @PostMapping
    public ResponseEntity<EventResponseDto> create(
            @Valid @RequestBody EventCreateRequestDto eventCreateRequestDto
    ) {
        LOGGER.info("Post request for create EventCreateRequestDto {}", eventCreateRequestDto);
        Event event = eventMapper.toDomain(eventCreateRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventMapper.toDto(eventService.create(event)));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("eventId") Long eventId
    ) {
        LOGGER.info("Delete request by event with id = {}", eventId);
        eventService.deleteById(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> findById(
            @PathVariable("eventId") Long eventId
    ) {
        LOGGER.info("Get request find By id = {}", eventId);
        return ResponseEntity.ok().body(eventMapper.toDto(eventService.findById(eventId)));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateById(
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto
            ) {
        LOGGER.info("Put request for update event By Id = {}, eventUpdateRequestDto = {}"
                , eventId, eventUpdateRequestDto);
        Event eventToUpdate = eventMapper.toDomain(eventUpdateRequestDto);
        return ResponseEntity.ok().body(eventMapper.toDto(eventService.update(eventId, eventToUpdate)));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventResponseDto>> findAllEvents(
            @RequestBody EventSearchRequestDto eventSearchRequestDto
            ) {
        LOGGER.info("Get request for search events by filter = {}" , eventSearchRequestDto);
        return ResponseEntity.ok().body(eventMapper.toDto(eventService.search(eventSearchRequestDto)));
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> findEventsByUserCreation() {
        LOGGER.info("Get request for find events creation by user");
        return ResponseEntity.ok().body(eventMapper.toDto(eventService.findAllEventsCreationByOwner()));
    }

}
