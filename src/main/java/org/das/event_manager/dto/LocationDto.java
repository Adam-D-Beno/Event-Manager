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
        @Min(value = 5, message = "Minimum location capacity of is 5 ")
        @Positive()
        @Digits(integer = Integer.MAX_VALUE, fraction = 0)
        Integer capacity,

        String description
) {
}
