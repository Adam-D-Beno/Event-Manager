package org.das.event_manager.dto;

import jakarta.validation.constraints.*;

public record SignUpRequest(
        @NotBlank(message = "Login should be not blank")
        @Size(min = 3, message = "Login length should be min 3")
        String login,

        @NotBlank(message = "Password should be not blank")
        @Size(min = 6, message = "Login length should be min 3")
        String password,

        @NotNull(message = "Age should be not NULL")
        @Positive(message = "Age should be positive")
        @Digits(integer = 150, fraction = 0, message = "age should be digits")
        Integer age
) {
}
