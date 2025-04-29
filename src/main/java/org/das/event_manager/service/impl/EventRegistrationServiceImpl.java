package org.das.event_manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.repository.RegistrationRepository;
import org.das.event_manager.service.EventRegistrationService;
import org.das.event_manager.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventRegistrationServiceImpl implements EventRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistrationServiceImpl.class);
    private final RegistrationRepository registrationRepository;
    private final EventMapper eventMapper;
    private final AuthenticationService authenticationService;
    private final EventService eventService;

    public EventRegistrationServiceImpl(
            RegistrationRepository registrationRepository,
            @Lazy EventMapper eventMapper,
            AuthenticationService authenticationService,
            EventService eventService
    ) {
        this.registrationRepository = registrationRepository;
        this.eventMapper = eventMapper;
        this.authenticationService = authenticationService;
        this.eventService = eventService;
    }

    @Override
    public void registerUserOnEvent(Long eventId) {
        LOGGER.info("Execute method registerUserOnEvent in RegistrationOnEventService, event id = {}",
                eventId);
        var event = eventService.findById(eventId);
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();
        if (currentAuthUser.id().equals(event.ownerId())) {
            throw new IllegalArgumentException("Owner cannot register own event=%s".formatted(event));
        }
        Optional<EventRegistrationEntity> registration =
                registrationRepository.findRegistration(event.id(), currentAuthUser.id());
        if (registration.isPresent()) {
            throw new IllegalArgumentException("User with id=%s already registered".formatted(currentAuthUser.id()));
        }
        checkStatusEvent(event);
        EventRegistrationEntity newRegistrationOnEvent = new EventRegistrationEntity(
                null,
                currentAuthUser.id(),
                eventMapper.toEntity(event),
                LocalDateTime.now()
        );
        registrationRepository.save(newRegistrationOnEvent);
    }

    @Override
    public void cancelOnRegistration(Long eventId) {
        LOGGER.info("Execute method cancelOnRegistration event id = {}", eventId);

        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();
        EventRegistrationEntity registrationEntity = registrationRepository
                .findRegistration(eventId, currentAuthUser.id())
                .orElseThrow(() -> new EntityNotFoundException("EventRegistration =%s not found, userId=%s"
                        .formatted(eventId, currentAuthUser.id())));

        if (registrationEntity.getEvent().getStatus() != EventStatus.STARTED) {
            LOGGER.error("Cannot cancel registration on event = {} has status = {}",
                    registrationEntity.getEvent(), registrationEntity.getEvent());
            throw new IllegalStateException("Cancellation of registration is impossible: " +
                    "the status of an event is not wait_start");
        }
        registrationRepository.delete(registrationEntity);
    }

    @Override
    public List<Event> findAllEventByUserRegistration() {
        LOGGER.info("Execute method findAllEventByUserRegistration in EventServiceImpl");
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();
        return registrationRepository.findAllRegisteredEventsByUserId(currentAuthUser.id())
                .stream()
                .map(eventMapper::toDomain)
                .toList();
    }

    private void checkStatusEvent(Event event) {
        LOGGER.info("Execute method checkStatusEvent in EventServiceImpl, event = {}", event);
        if (event.status() != EventStatus.WAIT_START) {
            LOGGER.error("Cannot registration event has status = {}",
                    event.status());
            throw new IllegalArgumentException("Event has status %s".formatted(event.status()));
        }
    }

}
