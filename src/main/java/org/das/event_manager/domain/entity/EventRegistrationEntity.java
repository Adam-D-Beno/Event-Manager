package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "date_registration")
    private LocalDateTime dateRegistration;
}
