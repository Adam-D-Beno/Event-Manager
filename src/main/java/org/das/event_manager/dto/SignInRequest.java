package org.das.event_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotBlank(message = "Login should be not blank")
        @Size(min = 3, message = "Login length should be min 3")
        String login,

        @NotBlank(message = "Password should be not blank")
        @Size(min = 6, message = "password length should be min 8")
        String password
) {
}
