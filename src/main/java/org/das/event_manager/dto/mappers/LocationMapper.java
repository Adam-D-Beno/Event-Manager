package org.das.event_manager.dto.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.entity.LocationEntity;
import org.das.event_manager.dto.LocationDto;

import java.util.List;

public interface LocationMapper {

    Location toDomain(@NotNull LocationDto locationDto);
    LocationDto toDto(@NotNull Location location);
    List<LocationDto> toDto(@NotNull List<Location> locations);
    LocationEntity toEntity(@NotNull Location location);
    Location toDomain(@NotNull LocationEntity locationEntity);
    List<Location> toDomain(@NotNull List<LocationEntity> locationEntities);

}
