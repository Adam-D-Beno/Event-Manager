package org.das.event_manager.dto.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.entity.LocationEntity;
import org.das.event_manager.dto.LocationDto;

import java.util.List;

public interface LocationMapper {

    Location toDomain(LocationDto locationDto);
    LocationDto toDto(Location location);
    List<LocationDto> toDto(List<Location> locations);
    LocationEntity toEntity(Location location);
    Location toDomain(LocationEntity locationEntity);
    List<Location> toDomain(List<LocationEntity> locationEntities);

}
