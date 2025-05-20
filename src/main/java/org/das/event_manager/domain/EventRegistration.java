package org.das.event_manager.domain;

import java.time.LocalDateTime;

public record EventRegistration(
        Long id,
        Long userId,
        Long eventId,
        LocalDateTime dateRegistration
) {}
