package org.das.event_manager.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.das.event_manager.domain.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private Long owner;

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
    private Long location;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<EventRegistrationEntity> registrations = new ArrayList<>();


    public EventEntity() {
    }

    public EventEntity(
            Long id,
            String name,
            Long owner,
            Integer maxPlaces,
            Integer occupiedPlaces,
            LocalDateTime date,
            BigDecimal cost,
            Integer duration,
            Long location,
            EventStatus status,
            List<EventRegistrationEntity> registrations
    ) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.location = location;
        this.status = status;
        this.registrations = registrations;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<EventRegistrationEntity> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<EventRegistrationEntity> registrations) {
        this.registrations = registrations;
    }
}
