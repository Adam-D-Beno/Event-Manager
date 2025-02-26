package org.das.event_manager.dto.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventUpdateRequestDto;

import java.util.List;

public interface EventMapper {

    Event toDomain(@NotNull EventCreateRequestDto eventUpdateRequestDto);
    Event toDomain(@NotNull EventUpdateRequestDto eventUpdateRequestDto);
    EventResponseDto toDto(@NotNull Event event);
    List<EventResponseDto> toDto(@NotNull List<Event> events);
    EventEntity toEntity(@NotNull Event event);
    Event toDomain(@NotNull EventEntity eventEntity);
    List<Event> toDomain(@NotNull List<EventEntity> eventEntities);
}
