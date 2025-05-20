package org.das.event_manager.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.das.event_manager.service.impl.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventMapper.class);
    private final AuthenticationService authenticationService;
    private final RegistrationMapper registrationMapper;

    public Event toDomain(EventCreateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventMapper class eventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return
                new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationService.getCurrentAuthenticatedUser().id(),
                eventUpdateRequestDto.maxPlaces(),
                List.of(),
                eventUpdateRequestDto.date(),
                eventUpdateRequestDto.cost(),
                eventUpdateRequestDto.duration(),
                eventUpdateRequestDto.locationId(),
                EventStatus.WAIT_START
        );
    }

    public Event toDomain(EventUpdateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventMapper class EventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationService.getCurrentAuthenticatedUser().id(),
                eventUpdateRequestDto.maxPlaces(),
                List.of(),
                eventUpdateRequestDto.date(),
                eventUpdateRequestDto.cost(),
                eventUpdateRequestDto.duration(),
                eventUpdateRequestDto.locationId(),
                null
        );
    }

    public EventResponseDto toDto(Event event) {
        LOGGER.info("Execute method to toDto in EventMapper class,  event = {}", event);
        return new EventResponseDto(
                event.id(),
                event.name(),
                authenticationService.getCurrentAuthenticatedUser().id(),
                event.maxPlaces(),
                event.registrations().size(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public List<EventResponseDto> toDto(List<Event> events) {
        LOGGER.info("Execute method to toDto in EventMapper class, events ={}", events);
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    public EventEntity toEntity(Event event) {
        LOGGER.info("Execute method to toEntity in EventEntityMapper class, event = {}",event);
        return new EventEntity(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.registrations().isEmpty()
                        ? List.of()
                        : registrationMapper.toEntity(event.registrations()),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }



    public Event toDomain(EventEntity eventEntity) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, event = {}",eventEntity);
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getRegistrations()
                        .stream()
                        .map(registrationMapper::toDomain).toList(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
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
