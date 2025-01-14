package org.das.event_manager.dto;

public record UserDto(
        Long id,
        String login,
        Integer age,
        String role
) {
}
