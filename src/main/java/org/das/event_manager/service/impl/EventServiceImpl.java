package org.das.event_manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.das.event_manager.domain.*;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.repository.EventRepository;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.EventService;
import org.das.event_manager.service.LocationService;
import org.das.event_manager.validation.EventValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final EventValidate eventValidate;

    public EventServiceImpl(
            EventRepository eventRepository,
            EventMapper eventMapper,
            LocationService locationService,
            AuthenticationService authenticationService,
            EventValidate eventValidate
    ) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.eventValidate = eventValidate;
    }

    @Override
    public Event create(Event eventForCreate) {
        LOGGER.info("Execute method create in EventServiceImpl, event = {}", eventForCreate);

        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();
        //check already exist event
        eventValidate.checkExistUser(currentAuthenticatedUser);
        eventValidate.checkExistLocation(eventForCreate.locationId());
        eventValidate.checkMaxPlacesMoreThenOnLocation(
                eventForCreate.maxPlaces(), locationService.getCapacity(eventForCreate.locationId())
        );

        //todo check th strline
        EventEntity eventEntity = eventMapper.toEntity(eventForCreate);
        eventEntity.setOwnerId(currentAuthenticatedUser.id());

        EventEntity saved = eventRepository.save(eventEntity);
        return eventMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long eventId) {
        LOGGER.info("Execute method create in EventServiceImpl, event id = {}", eventId);
        Event event = findById(eventId);
        eventValidate.checkStatusEvent(event.status());
        eventValidate.checkCurrentUserCanModify(event.ownerId());

        eventRepository.findById(eventId)
           .map(eventEntity -> {
                    eventEntity.setStatus(EventStatus.CANCELLED);
                   return eventRepository.save(eventEntity);
                }).orElseThrow(() -> new EntityNotFoundException("Event not found or status is not WAIT_START"));
    }

    @Override
    public Event findById(Long eventId) {
       LOGGER.info("Execute method findById in EventServiceImpl, event id = {}", eventId);
       return eventRepository.findById(eventId)
                .map(eventMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not found"
                        .formatted(eventId)));
    }

    @Transactional
    @Override
    public Event update(Long eventId, Event eventForUpdate) {

        eventValidate.checkDurationLessThenThirtyThrow(eventForUpdate.duration());
        eventValidate.checkMaxPlacesMoreCurrentMaxPlaces(findById(eventId), eventForUpdate);
        eventValidate.checkDatePastTime(eventForUpdate.date());
        eventValidate.checkCostMoreThenZero(eventForUpdate.cost());
        eventValidate.checkCurrentUserCanModify(eventId);
        eventValidate.checkStatusEvent(eventRepository.findEventStatusById(eventId));

        Location location = locationService.findById(eventForUpdate.locationId());
        EventEntity updated = eventRepository.findById(eventId)
                .map(eventEntity -> {
                    eventEntity.setId(eventId);
                    eventEntity.setName(eventForUpdate.name());
                    eventEntity.setMaxPlaces(eventForUpdate.maxPlaces());
                    eventEntity.setDate(eventForUpdate.date());
                    eventEntity.setCost(eventForUpdate.cost());
                    eventEntity.setDuration(eventForUpdate.duration());
                    eventEntity.setLocationId(location.id());
                    return eventRepository.save(eventEntity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not find"
                        .formatted(eventId)));
        return eventMapper.toDomain(updated);
    }

    @Override
    public List<Event> search(EventSearchRequestDto eventSearchRequestDto) {
        LOGGER.info("Execute method search in EventServiceImpl, eventSearchRequestDto = {}"
                , eventSearchRequestDto);

        List<EventEntity> searched = eventRepository.search(
                eventSearchRequestDto.name(),
                eventSearchRequestDto.placesMin(),
                eventSearchRequestDto.placesMax(),
                eventSearchRequestDto.dateStartAfter(),
                eventSearchRequestDto.dateStartBefore(),
                eventSearchRequestDto.costMin(),
                eventSearchRequestDto.costMax(),
                eventSearchRequestDto.durationMin(),
                eventSearchRequestDto.durationMax(),
                eventSearchRequestDto.locationId(),
                eventSearchRequestDto.status()
        );
        return eventMapper.toDomain(searched);
    }

    @Override
    public List<Event> findAllEventsCreationByOwner() {
        LOGGER.info("Execute method findAllEventsCreationByOwner in EventServiceImpl, eventSearchRequestDto");

        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();
        return eventMapper.toDomain(eventRepository.findEventsByOwner_Id((currentAuthUser.id())));
    }
}
