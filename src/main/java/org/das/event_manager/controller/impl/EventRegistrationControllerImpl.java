package org.das.event_manager.controller.impl;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.controller.EventRegistrationController;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.dto.mappers.impl.EventMapperImpl;
import org.das.event_manager.service.EventRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/events/registrations")
public class EventRegistrationControllerImpl implements EventRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistrationControllerImpl.class);
    private final EventRegistrationService eventRegistrationService;
    private final EventMapper eventMapper;

    public EventRegistrationControllerImpl(
            EventRegistrationService eventRegistrationService,
            EventMapper eventMapper
    ) {
        this.eventRegistrationService = eventRegistrationService;
        this.eventMapper = eventMapper;
    }


    @PostMapping("/{eventId}")
    @Override
    public ResponseEntity<Void> registrationUserOnEvent(
            @NotNull @PathVariable("eventId") Long eventId
    ) {
        LOGGER.info("Http post request for registration user on event id = {}", eventId);

        eventRegistrationService.registerUserOnEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{eventId}")
    @Override
    public ResponseEntity<Void> registrationUserCancelEvent(
            @NotNull @PathVariable("eventId") Long eventId
    ) {
        LOGGER.info("Http delete request for cancel on registration user on event id = {}", eventId);

        eventRegistrationService.cancelOnRegistration(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    @Override
    public ResponseEntity<List<EventResponseDto>> findAllEventsByUserRegistration() {
        LOGGER.info("Http get request for find all registration by user on event");

        return ResponseEntity.ok()
                .body(eventMapper.toDto(eventRegistrationService.findAllEventByUserRegistration()));
    }

}
