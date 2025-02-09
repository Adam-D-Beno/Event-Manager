package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.LocationEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventEntityMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventEntityMapper.class);

    public EventEntity toEntity(Event event) {
        LOGGER.info("Execute method to toEntity in EventEntityMapper class, event = {}",event);
        return new EventEntity(
                event.id(),
                event.name(),
                new UserEntity(event.ownerId()),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                new LocationEntity(event.locationId()),
                event.status()
        );
    }

    public Event toDomain(EventEntity eventEntity) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, event = {}",eventEntity);
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwner().getId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocation().getId(),
                eventEntity.getStatus()
        );
    }

    public List<Event> toDomain(List<EventEntity> eventEntities) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, eventEntities = {}",eventEntities);
        return eventEntities.stream()
                .map(this::toDomain)
                .toList();
    }
}
