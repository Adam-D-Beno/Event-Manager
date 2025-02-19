package org.das.event_manager.repository;

import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public  interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query("""
            SELECT ev FROM EventEntity ev
            WHERE (:name is null or ev.name = :name)
                AND (:placesMin is null  or ev.maxPlaces >= :placesMin)
                AND (:placesMax is null  or ev.maxPlaces <= :placesMax)
                AND (:dateStartAfter is null or ev.date >= :dateStartAfter)
                AND (:dateStartBefore is null or ev.date <= :dateStartBefore)
                AND (:costMin is null or ev.cost >= :costMin)
                AND (:costMax is null or ev.cost >= :costMax)
                AND (:durationMin is null or ev.duration >= :durationMin)
                AND (:durationMax is null or ev.duration >= :durationMax)
                AND (:locationId is null or ev.location.id = :locationId)
                AND (:status is null or ev.status = :status)
                
    """)
    List<EventEntity> search(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("cost")BigDecimal costMin,
            @Param("cost")BigDecimal costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long  locationId,
            @Param("status") EventStatus status
    );
}
