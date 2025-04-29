package org.das.event_manager.repository;

import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {

    @Query("""
            SELECT re.event FROM EventRegistrationEntity re
                        WHERE re.userId = :UserId
            """)
    List<EventEntity> findAllRegisteredEventsByUserId(
            @Param("UserId") Long UserId
    );

    @Query("""
            SELECT re FROM EventRegistrationEntity re
            WHERE re.event.id = :eventId
            AND re.userId = :userId
            """)
    Optional<EventRegistrationEntity> findRegistration(
            @Param("eventId") Long eventId,
            @Param("userId") Long userId
    );
}
