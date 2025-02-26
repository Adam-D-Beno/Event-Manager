package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.domain.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRegistrationService {

    void registerUserOnEvent(Long eventId);
    void cancelOnRegistration(Long eventId);
    List<Event> findAllEventByUserRegistration();
    EventRegistrationEntity findById(Long registrationId);
}
