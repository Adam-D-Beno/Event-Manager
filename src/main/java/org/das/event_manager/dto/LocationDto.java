package org.das.event_manager.dto;

import jakarta.validation.constraints.*;

public record LocationDto(
        @Null
        Long id,

        @NotBlank(message = "Location name should be not blank")
        String name,

        @NotBlank(message = "Location address should be not blank")
        String address,

        @NotNull
        @Min(value = 5, message = "Minimum location capacity is 5")
        @Positive(message = "capacity should be positive")
        @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "capacity should be digits")
        Integer capacity,

        String description
) {
}
