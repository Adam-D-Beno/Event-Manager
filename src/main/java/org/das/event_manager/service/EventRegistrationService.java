package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.dto.mappers.UserMapper;
import org.das.event_manager.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistrationService.class);
    private final RegistrationRepository registrationRepository;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    public EventRegistrationService(
            RegistrationRepository registrationRepository,
            @Lazy EventMapper eventMapper,
            UserMapper userMapper,
            AuthenticationService authenticationService
    ) {
        this.registrationRepository = registrationRepository;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
    }


    public void registerUserOnEvent(Long eventId) {
        LOGGER.info("Execute method registerUserOnEvent in RegistrationOnEventService, event id = {}",
                eventId);

        EventEntity eventFound = registrationRepository.findEventById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not find"
                        .formatted(eventId)));

        checkStatusEvent(eventFound);

        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        UserEntity userEntity = userMapper.toEntity(currentAuthUser);

        EventRegistrationEntity newRegistrationOnEvent = new EventRegistrationEntity(
                null,
                userEntity,
                eventFound,
                LocalDateTime.now()
        );
        registrationRepository.save(newRegistrationOnEvent);
    }


    public void cancelOnRegistration(Long eventId) {
        LOGGER.info("Execute method cancelOnRegistration event id = {}", eventId);

        EventRegistrationEntity registrationEntity = registrationRepository
                .getRegistrationsByEventId(eventId)
                .orElseThrow(() -> new EntityNotFoundException("EventRegistration not found or status is not WAIT_START"));

        if (registrationEntity.getEvent().getStatus() != EventStatus.STARTED) {
            LOGGER.error("Cannot cancel registration on event = {} has status = {}",
                    registrationEntity.getEvent(), registrationEntity.getEvent());

            throw new IllegalStateException("Cancellation of registration is impossible: " +
                    "the status of an event is not wait_start");
        }
        registrationRepository.delete(registrationEntity);
    }

    public List<Event> findAllEventByUserRegistration() {
        LOGGER.info("Execute method findAllEventByUserRegistration in EventService");

        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        return registrationRepository.getRegistrationsByUserId(currentAuthUser.id())
                .stream()
                .map(reg -> eventMapper.toDomain(reg.getEvent()))
                .toList();
    }


    public EventRegistrationEntity findById(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() -> new EntityNotFoundException("EventRegistration not found"));

    }

    private void checkStatusEvent(EventEntity eventEntity) {
        LOGGER.info("Execute method checkStatusEvent in EventService, event = {}", eventEntity);
        if (eventEntity.getStatus() != null && eventEntity.getStatus() == EventStatus.CANCELLED
                || eventEntity.getStatus() == EventStatus.FINISHED) {
            LOGGER.error("Cannot registration event has status = {}",
                    eventEntity.getStatus());

            throw new IllegalArgumentException("Event has status %s".formatted(eventEntity.getStatus()));
        }
    }

}
