package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.Location;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventDto;
import org.das.event_manager.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDtoMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventDtoMapper.class);

    public Event toDomain(EventCreateRequestDto eventCreateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventDtoMapper class eventCreateRequestDto = {}",
                eventCreateRequestDto);
        //todo replace ownerId
        return new Event(
                null,
                eventCreateRequestDto.name(),
                null,
                eventCreateRequestDto.maxPlaces(),
                null,
                eventCreateRequestDto.date(),
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                EventStatus.WAIT_START

        );
    }

    public EventDto toDto(Event event) {
        LOGGER.info("Execute method to toDto in EventDtoMapper class,  event = {}", event);
        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public List<EventDto> toDto(List<Event> events) {
        LOGGER.info("Execute method to toDto in EventDtoMapper class, events ={}", events);
        return events.stream()
                .map(this::toDto)
                .toList();
    }
}
