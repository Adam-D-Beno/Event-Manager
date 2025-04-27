package org.das.event_manager.dto.mappers.impl;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventRegistration;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.dto.mappers.RegistrationMapper;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.impl.AuthenticationServiceImpl;
import org.das.event_manager.service.impl.EventRegistrationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMapperImpl implements EventMapper {

    private final Integer initDefaultOccupiedPlaces = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventMapperImpl.class);
    private final AuthenticationService authenticationServiceImpl;
    private final RegistrationMapper registrationMapper;

    public EventMapperImpl(
            AuthenticationService authenticationServiceImpl,
           @Lazy RegistrationMapper registrationMapper
    ) {
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.registrationMapper = registrationMapper;
    }

    @Override
    public Event toDomain(EventCreateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventMapperImpl class eventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return
                new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationServiceImpl.getCurrentAuthenticatedUser().id(),
                eventUpdateRequestDto.maxPlaces(),
                initDefaultOccupiedPlaces,
                eventUpdateRequestDto.date(),
                eventUpdateRequestDto.cost(),
                eventUpdateRequestDto.duration(),
                eventUpdateRequestDto.locationId(),
                EventStatus.WAIT_START,
                List.of()
        );
    }

    @Override
    public Event toDomain(EventUpdateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventMapperImpl class EventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationServiceImpl.getCurrentAuthenticatedUser().id(),
                eventUpdateRequestDto.maxPlaces(),
                initDefaultOccupiedPlaces,
                eventUpdateRequestDto.date(),
                eventUpdateRequestDto.cost(),
                eventUpdateRequestDto.duration(),
                eventUpdateRequestDto.locationId(),
                EventStatus.WAIT_START,
                List.of()
        );
    }

    @Override
    public EventResponseDto toDto(Event event) {
        LOGGER.info("Execute method to toDto in EventMapperImpl class,  event = {}", event);
        return new EventResponseDto(
                event.id(),
                event.name(),
                authenticationServiceImpl.getCurrentAuthenticatedUser().id(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    @Override
    public List<EventResponseDto> toDto(List<Event> events) {
        LOGGER.info("Execute method to toDto in EventMapperImpl class, events ={}", events);
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public EventEntity toEntity(Event event) {
        LOGGER.info("Execute method to toEntity in EventEntityMapper class, event = {}",event);
        return new EventEntity(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status(),
                event.registrations().isEmpty()
                        ? List.of()
                        : registrationMapper.toEntity(event.registrations())
        );
    }

    @Override
    public Event toDomain(EventEntity eventEntity) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, event = {}",eventEntity);
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getId(),
                eventEntity.getStatus(),
                eventEntity.getRegistrations()
                        .stream()
                        .map(registrationMapper::toDomain).toList()
        );
    }

    @Override
    public List<Event> toDomain(List<EventEntity> eventEntities) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, eventEntities = {}",eventEntities);
        return eventEntities.stream()
                .map(this::toDomain)
                .toList();
    }
}
