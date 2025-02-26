package org.das.event_manager.domain;

import java.time.LocalDateTime;

public record EventRegistration(
        Long id,
        User user,
        Event event,
        LocalDateTime dateRegistration
) {}
