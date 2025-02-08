package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.LocationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventEntityMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventEntityMapper.class);

    public EventEntity toEntity(Event event) {
        LOGGER.info("Execute method to toEntity in EventEntityMapper class, event = {}",event);
        return null;
    }

    public Event toDomain(EventEntity eventEntity) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, event = {}",eventEntity);
        return null;
    }

    public List<Event> toDomain(List<EventEntity> eventEntities) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, eventEntities = {}",eventEntities);
        return null;
    }
}
