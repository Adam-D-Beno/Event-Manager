package org.das.event_manager.repository;

import org.das.event_manager.domain.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query
    List<EventEntity> search(

    );
}
