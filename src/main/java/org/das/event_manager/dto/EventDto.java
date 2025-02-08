package org.das.event_manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
     LocalDateTime date,
     BigDecimal cost,
     Integer duration,
     Integer locationId,
     EventStatus status
) {}
