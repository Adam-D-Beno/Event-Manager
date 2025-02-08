package org.das.event_manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventCreateRequestDto(
        String name,
        Integer maxPlaces,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime date,
        BigDecimal cost,
        Integer duration,
        Integer locationId
) {
}
