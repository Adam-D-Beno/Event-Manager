package org.das.event_manager.repository;

import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, LocationRepository> {

    @Query("""
            SELECT re.event FROM RegistrationEntity re
                        WHERE re.event.id = :eventId
            """)
    Optional<RegistrationEntity> findByEventId(
            @Param("eventId") Long eventId);
}
