package org.das.event_manager.repository;

import org.das.event_manager.domain.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, LocationRepository> {
    List<RegistrationEntity> findAllByUser_Id(Long userId);
}
