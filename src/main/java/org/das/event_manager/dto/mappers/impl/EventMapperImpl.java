package org.das.event_manager.dto.mappers.impl;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.domain.entity.LocationEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.service.impl.AuthenticationServiceImpl;
import org.das.event_manager.service.impl.EventRegistrationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Component
public class EventMapperImpl implements EventMapper {

    private final Integer initDefaultOccupiedPlaces = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventMapperImpl.class);
    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final EventRegistrationServiceImpl registrationService;

    public EventMapperImpl(AuthenticationServiceImpl authenticationServiceImpl, EventRegistrationServiceImpl registrationService) {
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.registrationService = registrationService;
    }

    @Override
    public Event toDomain(@NotNull EventCreateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventMapperImpl class eventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return
                new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationServiceImpl.getCurrentAuthenticatedUserOrThrow().id(),
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
    public Event toDomain(@NotNull EventUpdateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventMapperImpl class EventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationServiceImpl.getCurrentAuthenticatedUserOrThrow().id(),
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
    public EventResponseDto toDto(@NotNull Event event) {
        LOGGER.info("Execute method to toDto in EventMapperImpl class,  event = {}", event);
        return new EventResponseDto(
                event.id(),
                event.name(),
                authenticationServiceImpl.getCurrentAuthenticatedUserOrThrow().id(),
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
    public List<EventResponseDto> toDto(@NotNull List<Event> events) {
        LOGGER.info("Execute method to toDto in EventMapperImpl class, events ={}", events);
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public EventEntity toEntity(@NotNull Event event) {
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
                event.status(),
                event.registrations()
                        .stream()
                        .map(registrationService::findById).toList()
        );
    }

    @Override
    public Event toDomain(@NotNull EventEntity eventEntity) {
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
                eventEntity.getStatus(),
                eventEntity.getRegistrations().stream().map(EventRegistrationEntity::getId).toList()
        );
    }

    @Override
    public List<Event> toDomain(@NotNull List<EventEntity> eventEntities) {
        LOGGER.info("Execute method to toDomain in EventEntityMapper class, eventEntities = {}",eventEntities);
        return eventEntities.stream()
                .map(this::toDomain)
                .toList();
    }

}
