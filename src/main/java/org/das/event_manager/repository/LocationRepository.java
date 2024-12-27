package org.das.event_manager.repository;

import org.das.event_manager.entity.LocationEntity;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("""
            UPDATE LocationEntity l
            SET
                    l.id = :id,
                    l.name = :name,
                    l.address = :address,
                    l.capacity = :capacity,
                    l.description = :description
            WHERE
                    l.id = :id
            """)
    LocationEntity update(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("address") String address,
            @Param("capacity") Integer capacity,
            @Param("description") String description
    );
}
