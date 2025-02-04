package org.das.event_manager.domain.entity;

import org.das.event_manager.domain.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventEntity {
    private Long id;
    private String name;
    private String ownerId;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    private LocalDateTime date;
    private BigDecimal cost;
    private Integer duration;
    private Integer locationId;
    private EventStatus status;
}
