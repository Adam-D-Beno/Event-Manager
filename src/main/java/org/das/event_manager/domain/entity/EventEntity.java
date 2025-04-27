package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.das.event_manager.domain.EventStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "max_Places", nullable = false)
    private Integer maxPlaces;

    @Column(name = "occupied_Places")
    private Integer occupiedPlaces;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<EventRegistrationEntity> registrations;


    public EventEntity() {
    }

    public EventEntity(
            Long id,
            String name,
            Long ownerId,
            Integer maxPlaces,
            Integer occupiedPlaces,
            LocalDateTime date,
            BigDecimal cost,
            Integer duration,
            Long locationId,
            EventStatus status,
            List<EventRegistrationEntity> registrations
    ) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
        this.registrations = registrations;
    }
}
