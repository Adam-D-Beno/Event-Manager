package org.das.event_manager.repository;

import org.das.event_manager.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    boolean existsByName(String name);

    boolean existsByAddress(String address);

    boolean existsById(Long id);

    Integer getCapacityById(Long id);

    @Transactional
    @Modifying
    @Query("""
            UPDATE LocationEntity l
            SET
                    l.name = :name,
                    l.address = :address,
                    l.capacity = :capacity,
                    l.description = :description
            WHERE
                    l.id = :id
            """)
    LocationEntity update(
            @Param("name") String name,
            @Param("address") String address,
            @Param("capacity") Integer capacity,
            @Param("description") String description
    );
}
