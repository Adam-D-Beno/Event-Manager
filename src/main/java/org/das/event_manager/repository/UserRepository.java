package org.das.event_manager.repository;

import org.das.event_manager.entity.LocationEntity;
import org.das.event_manager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
