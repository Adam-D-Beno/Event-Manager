package org.das.event_manager.domain;


public record User(
        Long id,
        String login,
        String passwordHash,
        Integer age,
        UserRole userRole
) {
}
