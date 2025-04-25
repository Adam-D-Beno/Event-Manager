package org.das.event_manager.service;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.entity.EventRegistrationEntity;

import java.util.List;

public interface EventRegistrationService {

    void registerUserOnEvent(Long eventId);
    void cancelOnRegistration(Long eventId);
    List<Event> findAllEventByUserRegistration();
    EventRegistrationEntity findById(Long registrationId);
}
