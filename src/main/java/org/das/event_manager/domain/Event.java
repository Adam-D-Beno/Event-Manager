package org.das.event_manager.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public record Event(
        Long id,
        String name,
        Long ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        ZonedDateTime date,
        BigDecimal cost,
        Integer duration,
        Long locationId,
        EventStatus status,
        List<Long> registrations
) {}
