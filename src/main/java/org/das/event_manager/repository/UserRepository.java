package org.das.event_manager.repository;

import org.das.event_manager.entity.LocationEntity;
import org.das.event_manager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);
}
