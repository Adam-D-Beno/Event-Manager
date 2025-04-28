package org.das.event_manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record EventUpdateRequestDto(

        String name,

        @Min(value = 1, message = "Minimum maxPlaces is 1")
        @Positive(message = "maxPlaces should be positive")
        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "maxPlaces should be digits")
        Integer maxPlaces,

        @Future
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        LocalDateTime date,

        @Positive(message = "cost should be positive")
        @DecimalMin(value = "1", message = "cost should be more then zero")
        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "cost should be digits")
        BigDecimal cost,

        @Positive(message = "duration should be positive")
        @Min(value = 30, message = "Minimum duration is 30")
        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "duration should be digits")
        Integer duration,

        @Positive(message = "locationId should be positive")
        @Min(value = 1, message = "Minimum locationId is 1")
        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "locationId should be digits")
        Long locationId
) {
}
