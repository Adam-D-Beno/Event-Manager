package org.das.event_manager.service;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.*;
import org.das.event_manager.dto.EventSearchRequestDto;

import java.util.List;

public interface EventService {

     Event create(@NotNull Event event);
     void deleteById(@NotNull Long eventId);
     Event findById(@NotNull Long eventId);
     Event update(@NotNull Long eventId, @NotNull Event eventToUpdate);
     List<Event> search(EventSearchRequestDto eventSearchRequestDto);
     List<Event> findAllEventsCreationByOwner();
}
