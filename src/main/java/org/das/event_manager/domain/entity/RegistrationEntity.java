package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations", uniqueConstraints =
            @UniqueConstraint(columnNames = {"user_id", "event_id"}))
@Setter
@Getter
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "date_registration")
    private LocalDateTime dateRegistration;

    public RegistrationEntity(
            Long id,
            EventEntity event,
            LocalDateTime dateRegistration
    ) {
        this.id = id;
        this.event = event;
        this.dateRegistration = dateRegistration;
    }

    public RegistrationEntity() {

    }
}
