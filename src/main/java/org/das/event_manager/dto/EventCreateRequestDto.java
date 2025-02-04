package org.das.event_manager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventCreateRequestDto(
        String name,
        Integer maxPlaces,
        LocalDateTime date,
        BigDecimal cost,
        Integer duration,
        Integer locationId
) {
}
