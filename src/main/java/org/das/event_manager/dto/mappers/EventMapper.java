package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.dto.EventCreateRequestDto;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.EventUpdateRequestDto;

import java.util.List;

public interface EventMapper {

    Event toDomain(EventCreateRequestDto eventUpdateRequestDto);
    Event toDomain(EventUpdateRequestDto eventUpdateRequestDto);
    EventResponseDto toDto(Event event);
    List<EventResponseDto> toDto(List<Event> events);
    EventEntity toEntity(Event event);
    Event toDomain(EventEntity eventEntity);
    List<Event> toDomain(List<EventEntity> eventEntities);
}
