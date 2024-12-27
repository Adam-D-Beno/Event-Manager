package org.das.event_manager.dto;

import jakarta.validation.constraints.*;

public record LocationDto(
        @Null
        Long id,
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String address,
        @Min(5)
        @Positive()
        @Digits(integer = Integer.MAX_VALUE, fraction = 0)
        Integer capacity,
        String description
) {
}
