package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Setter
@Getter
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "date")
    private LocalDateTime dateRegistration;

    public RegistrationEntity(Long id, UserEntity userEntity, EventEntity eventEntity) {
    }

    public RegistrationEntity() {

    }
}
