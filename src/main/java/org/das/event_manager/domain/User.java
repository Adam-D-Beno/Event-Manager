package org.das.event_manager.domain;

import jakarta.validation.constraints.*;
import org.das.event_manager.utils.Role;


public record User(
        Long id,
        String login,
        String password,
        Integer age,
        Role role
) {
}
