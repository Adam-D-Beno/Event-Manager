package org.das.event_manager.domain;

import org.das.event_manager.utils.Role;


public record User(
        Long id,
        String login,
        String passwordHash,
        Integer age,
        Role role
) {
}
