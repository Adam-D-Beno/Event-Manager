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

    public EventService(
            EventRepository eventRepository,
            EventEntityMapper eventEntityMapper
    ) {
        this.eventRepository = eventRepository;
        this.eventEntityMapper = eventEntityMapper;
    }


    public Event create(Event event) {
        LOGGER.info("Execute method create in EventService, event = {}", event);
        EventEntity eventEntity = eventEntityMapper.toEntity(event);
        return eventEntityMapper.toDomain(eventRepository.save(eventEntity));
    }
}
