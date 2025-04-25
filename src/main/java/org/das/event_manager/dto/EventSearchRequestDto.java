package org.das.event_manager.dto;

import org.das.event_manager.domain.EventStatus;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(

      String name,
      Integer placesMin,
      Integer placesMax,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime dateStartAfter,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime dateStartBefore,
      BigDecimal costMin,
      BigDecimal costMax,
      Integer durationMin,
      Integer durationMax,
      Long locationId,
      EventStatus status
) {
}
