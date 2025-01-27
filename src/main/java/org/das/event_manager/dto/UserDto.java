package org.das.event_manager.dto;

import org.das.event_manager.domain.UserRole;

public record UserDto(
        Long id,
        String login,
        Integer age,
        UserRole userRole
) {
}
