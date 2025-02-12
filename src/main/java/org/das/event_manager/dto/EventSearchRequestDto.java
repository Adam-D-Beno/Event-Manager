package org.das.event_manager.dto;

import jakarta.validation.constraints.*;
import org.das.event_manager.domain.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(

      @NotBlank(message = "Name must not be blank")
      String name,

      @NotNull
      @Min(value = 1, message = "Minimum maxPlaces is 1")
      @Positive(message = "maxPlaces should be positive")
      @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "maxPlaces should be digits")
      Integer placesMin,

      @NotNull
      @Min(value = 1, message = "Minimum maxPlaces is 1")
      @Positive(message = "maxPlaces should be positive")
      @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "maxPlaces should be digits")
      Integer placesMax,

      @FutureOrPresent(message = "dateStartAfter should be future or present")
      LocalDateTime dateStartAfter,

      @FutureOrPresent(message = "dateStartBefore should be future or present")
      LocalDateTime dateStartBefore,

      @NotNull
      @Positive(message = "cost should be positive")
      @DecimalMin(value = "1", message = "cost should be more then zero")
      @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "cost should be digits")
      BigDecimal costMin,

      @NotNull
      @Positive(message = "cost should be positive")
      @DecimalMin(value = "1", message = "cost should be more then zero")
      @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "cost should be digits")
      BigDecimal costMax,

      @NotNull
      @Positive(message = "duration should be positive")
      @Min(value = 1, message = "Minimum duration is 1")
      @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "duration should be digits")
      Integer durationMin,

      @NotNull
      @Positive(message = "duration should be positive")
      @Min(value = 1, message = "Minimum duration is 1")
      @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "duration should be digits")
      Integer durationMax,

      @NotNull
      @Positive(message = "locationId should be positive")
      @Min(value = 1, message = "Minimum locationId is 1")
      @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "locationId should be digits")
      Long locationId,

      @NotBlank
      EventStatus status
) {
}
