package org.das.event_manager.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Event(
        Long id,
        String name,
        Long ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        LocalDateTime date,
        BigDecimal cost,
        Integer duration,
        Long locationId,
        EventStatus status
) {}
