package org.das.event_manager.domain;

import jakarta.validation.constraints.*;

import javax.management.relation.Role;

public record User(
        Long id,
        String login,
        String password,
        Integer age,
        String role
) {
}
