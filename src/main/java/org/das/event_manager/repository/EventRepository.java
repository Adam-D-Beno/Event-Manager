package org.das.event_manager.repository;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public  interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query("""
            SELECT ev FROM EventEntity ev
            WHERE (:name is null or ev.name LIKE %:name%)
                AND (:placesMin is null  or ev.maxPlaces >= :placesMin)
                AND (:placesMax is null  or ev.maxPlaces <= :placesMax)
                AND (CAST(:dateStartAfter as date) is null or ev.date >= :dateStartAfter)
                AND (CAST(:dateStartBefore as date) is null or ev.date <= :dateStartBefore)
                AND (:costMin is null or ev.cost >= :costMin)
                AND (:costMax is null or ev.cost <= :costMax)
                AND (:durationMin is null or ev.duration >= :durationMin)
                AND (:durationMax is null or ev.duration <= :durationMax)
                AND (:locationId is null or ev.locationId = :locationId)
                AND (:status is null or ev.status = :status)
    """)
    List<EventEntity> search(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin")BigDecimal costMin,
            @Param("costMax")BigDecimal costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long  locationId,
            @Param("status") EventStatus status
    );


    List<EventEntity> findEventsByOwner_Id(Long ownerId);

    @Query("""
        select ev.id from EventEntity ev 
        WHERE ev.status = :status
        and ev.date < CURRENT_TIMESTAMP 
    """)
    List<Long> findStartedEventsWithStatus(@Param("status") EventStatus status);

    @Query(value = """
      SELECT ev.id from events ev
          where ev.date + INTERVAL '1 MINUTE' * ev.duration < CURRENT_TIMESTAMP
    """, nativeQuery = true)
    List<Long> findEndedEventsWithStatus(@Param("status") EventStatus status);

    @Modifying
    @Transactional
    @Query("""
        update EventEntity ev
        set ev.status = :status
        where ev.id = :event_id
    """)
    void changeEventStatus(
            @Param("event_id") Long eventId,
            @Param("status") EventStatus status
    );

    @Query("""
        select ev.status from EventEntity ev
            where ev.id = :eventId
        """)
    EventStatus findEventStatusById(@Param("eventId") Long eventId);
}
