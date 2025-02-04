package org.das.event_manager.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Event(
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
