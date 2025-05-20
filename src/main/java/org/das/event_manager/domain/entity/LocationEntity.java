package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
}
