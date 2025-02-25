package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.Registration;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.RegistrationEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.mappers.EventEntityMapper;
import org.das.event_manager.dto.mappers.RegistrationEntityMapper;
import org.das.event_manager.dto.mappers.UserEntityMapper;
import org.das.event_manager.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationOnEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationOnEventService.class);
    private final RegistrationRepository registrationRepository;
    private final EventEntityMapper eventEntityMapper;
    private final RegistrationEntityMapper registrationEntityMapper;

    public RegistrationOnEventService(
            RegistrationRepository registrationRepository,
            EventEntityMapper eventEntityMapper,
            RegistrationEntityMapper registrationEntityMapper
    ) {
        this.registrationRepository = registrationRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.registrationEntityMapper = registrationEntityMapper;
    }

    public void registerUserOnEvent(Event event, User currentAuthUser) {
        LOGGER.info("Execute method registerUserOnEvent in RegistrationOnEventService, event = {}, user = {}",
                event, currentAuthUser);
        EventEntity eventEntity = eventEntityMapper.toEntity(event);
        RegistrationEntity newRegistrationOnEvent = new RegistrationEntity(
                null,
                eventEntity,
                LocalDateTime.now()
        );
        registrationRepository.save(newRegistrationOnEvent);
    }

    public void cancelOnRegistration(Long eventId, User currentAuthUser) {
        LOGGER.info("Execute method cancelOnRegistration event id = {},user = {}", eventId, currentAuthUser);
        RegistrationEntity registrationEntity = registrationRepository
                .findByEventId(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found or status is not WAIT_START"));

        if (registrationEntity.getEvent().getStatus() != EventStatus.STARTED) {
            LOGGER.error("Cannot cancel registration on event = {} has status = {}",
                    registrationEntity.getEvent(), registrationEntity.getEvent());
            throw new IllegalStateException("Cancellation of registration is impossible: " +
                    "the status of an event is not wait_start");
        }
        registrationRepository.delete(registrationEntity);
    }
}
