package org.das.event_manager.repository;

import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {

    @Query("""
            SELECT re FROM EventRegistrationEntity re
                        WHERE re.event.id = :eventId
            """)
    Optional<EventRegistrationEntity> getRegistrationsByEventId(
            @Param("eventId") Long eventId
    );

    @Query("""
            SELECT re FROM EventRegistrationEntity re
                        WHERE re.userEntity.id = :UserId
            """)
    List<EventRegistrationEntity> getRegistrationsByUserId(
            @Param("UserId") Long UserId
    );

    @Query("""
            SELECT re.event FROM EventRegistrationEntity re
            WHERE re.event.id = :eventId
            """)
    Optional<EventEntity> findEventById(@Param("eventId") Long eventId);
}
