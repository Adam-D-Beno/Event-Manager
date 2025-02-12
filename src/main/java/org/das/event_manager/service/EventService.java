package org.das.event_manager.service;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.dto.mappers.EventEntityMapper;
import org.das.event_manager.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventEntityMapper eventEntityMapper;
    private final LocationService locationService;

    public EventService(
            EventRepository eventRepository,
            EventEntityMapper eventEntityMapper,
            LocationService locationService
    ) {
        this.eventRepository = eventRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.locationService = locationService;
    }


    public Event create(Event event) {
        LOGGER.info("Execute method create in EventService, event = {}", event);
        checkMaxPlaces(event);
        EventEntity eventEntity = eventEntityMapper.toEntity(event);
        EventEntity saved = eventRepository.save(eventEntity);
        return eventEntityMapper.toDomain(saved);
    }

    public void checkMaxPlaces(Event event) {
        LOGGER.info("Checking max places for event: {}", event);
        Integer locationCapacity = locationService.getCapacity(event.locationId());
        if (locationCapacity == null) {
            LOGGER.error("Location capacity is null for locationId: {}", event.locationId());
            throw new IllegalArgumentException("Location capacity cannot be null");
        }
        if (event.maxPlaces() > locationCapacity) {
            LOGGER.error("Error, Maximum number = {} of places at the event more then location capacity = {} ",
                    event.maxPlaces(), locationCapacity);
            throw new IllegalArgumentException("maxPlaces cannot be more then location maxPlaces");
        }
    }

}
