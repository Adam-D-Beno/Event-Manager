package org.das.event_manager.domain;

public record Location(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}
