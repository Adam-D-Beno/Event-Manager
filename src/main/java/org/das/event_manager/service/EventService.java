package org.das.event_manager.service;

import org.das.event_manager.domain.*;
import org.das.event_manager.dto.EventSearchRequestDto;

import java.util.List;

public interface EventService {

     Event create(Event eventForCreate);
     void deleteById(Long eventId);
     Event findById(Long eventId);
     Event update(Long eventId, Event eventToUpdate);
     List<Event> search(EventSearchRequestDto eventSearchRequestDto);
     List<Event> findAllEventsCreationByOwner();
     List<Long>findStartedEventsWithStatus(EventStatus status);
     List<Long>findEndedEventsWithStatus(EventStatus status);
     void changeEventStatuses(List<Long> eventIds, EventStatus status);
}
