package org.das.event_manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EventController {
    ResponseEntity<EventResponseDto> create(EventCreateRequestDto eventCreateRequestDto);
    ResponseEntity<Void> deleteById(Long eventId);
    ResponseEntity<EventResponseDto> findById(Long eventId);
    ResponseEntity<EventResponseDto> updateById(Long eventId, EventUpdateRequestDto eventUpdateRequestDto);
    ResponseEntity<List<EventResponseDto>> findAllEvents(EventSearchRequestDto eventSearchRequestDto);
    ResponseEntity<List<EventResponseDto>> findEventsByUserCreation();

}
