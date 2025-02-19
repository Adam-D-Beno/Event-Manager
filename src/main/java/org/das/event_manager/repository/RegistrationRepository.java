package org.das.event_manager.repository;

import org.das.event_manager.domain.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, LocationRepository> {
}
