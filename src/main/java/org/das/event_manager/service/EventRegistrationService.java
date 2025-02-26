package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.domain.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRegistrationService {

    void registerUserOnEvent(@NotNull Long eventId);
    void cancelOnRegistration(@NotNull Long eventId);
    List<Event> findAllEventByUserRegistration();
    EventRegistrationEntity findById(@NotNull Long registrationId);
}
