package org.das.event_manager.dto;

import org.das.event_manager.domain.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(
      String name,
      Integer placesMin,
      Integer placesMax,
      LocalDateTime dateStartAfter,
      LocalDateTime dateStartBefore,
      BigDecimal costMin,
      BigDecimal costMax,
      Integer durationMin,
      Integer durationMax,
      Long locationId,
      EventStatus status
) {
}
