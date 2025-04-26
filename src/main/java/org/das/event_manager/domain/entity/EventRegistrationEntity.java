package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Setter
@Getter
public class EventRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "date_registration")
    private LocalDateTime dateRegistration;

    public EventRegistrationEntity(
            Long id,
            Long userId,
            EventEntity event,
            LocalDateTime dateRegistration
    ) {
        this.id = id;
        this.userId = userId;
        this.event = event;
        this.dateRegistration = dateRegistration;
    }

    public EventRegistrationEntity() {

    }
}
