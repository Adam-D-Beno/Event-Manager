package org.das.event_manager.dto.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventUpdateRequestDto;
import org.das.event_manager.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Component
public class EventDtoMapper {

    private final Integer initDefaultOccupiedPlaces = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventDtoMapper.class);
    private final AuthenticationService authenticationService;

    public EventDtoMapper(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public Event toDomain(@NotNull EventCreateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventDtoMapper class eventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationService.getCurrentAuthenticatedUserOrThrow().id(),
                eventUpdateRequestDto.maxPlaces(),
                initDefaultOccupiedPlaces,
                eventUpdateRequestDto.date(),
                eventUpdateRequestDto.cost(),
                eventUpdateRequestDto.duration(),
                eventUpdateRequestDto.locationId(),
                EventStatus.WAIT_START
        );
    }

    public Event toDomain(@NotNull EventUpdateRequestDto eventUpdateRequestDto) {
        LOGGER.info("Execute method to toDomain in EventDtoMapper class EventUpdateRequestDto = {}",
                eventUpdateRequestDto);
        return new Event(
                null,
                eventUpdateRequestDto.name(),
                authenticationService.getCurrentAuthenticatedUserOrThrow().id(),
                eventUpdateRequestDto.maxPlaces(),
                initDefaultOccupiedPlaces,
                eventUpdateRequestDto.date(),
                eventUpdateRequestDto.cost(),
                eventUpdateRequestDto.duration(),
                eventUpdateRequestDto.locationId(),
                EventStatus.WAIT_START
        );
    }

    public EventResponseDto toDto(@NotNull Event event) {
        LOGGER.info("Execute method to toDto in EventDtoMapper class,  event = {}", event);
        return new EventResponseDto(
                event.id(),
                event.name(),
                authenticationService.getCurrentAuthenticatedUserOrThrow().id(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public List<EventResponseDto> toDto(@NotNull List<Event> events) {
        LOGGER.info("Execute method to toDto in EventDtoMapper class, events ={}", events);
        return events.stream()
                .map(this::toDto)
                .toList();
    }

}
