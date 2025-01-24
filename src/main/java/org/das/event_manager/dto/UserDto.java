package org.das.event_manager.dto;

import org.das.event_manager.utils.Role;

public record UserDto(
        Long id,
        String login,
        Integer age,
        Role role
) {
}
