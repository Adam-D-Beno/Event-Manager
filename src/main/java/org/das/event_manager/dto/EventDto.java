package org.das.event_manager.dto;

import org.das.event_manager.domain.EventStatus;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;

public record EventDto(
     Integer id,
     String name,
     String ownerId,
     Integer maxPlaces,
     Integer occupiedPlaces,
     LocalDateTime date,
     BigDecimal cost,
     Integer duration,
     Integer locationId,
     EventStatus status
) {}
