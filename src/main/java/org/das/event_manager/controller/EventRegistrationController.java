package org.das.event_manager.controller;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.dto.EventResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface EventRegistrationController {

    ResponseEntity<Void> registrationUserOnEvent(Long eventId);
    ResponseEntity<Void> registrationUserCancelEvent(Long eventId);
    ResponseEntity<List<EventResponseDto>> findAllEventsByUserRegistration();
}
