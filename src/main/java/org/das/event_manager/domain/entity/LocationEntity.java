package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Setter
@Getter
public class LocationEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name", nullable = false, unique = true)
   private String name;

   @Column(name = "address", nullable = false, unique = true)
   private String address;

   @Column(name = "capacity", nullable = false)
   private Integer capacity;

   @Column(name = "description")
   private String description;

    public LocationEntity(Long id) {
        this.id = id;
    }

    public LocationEntity(
            Long id,
            String name,
            String address,
            Integer capacity,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.description = description;
    }

    public LocationEntity() {

    }
}
