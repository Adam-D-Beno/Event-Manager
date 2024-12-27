package org.das.event_manager.converters;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Location;
import org.das.event_manager.entity.LocationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Validated
@Component
public class LocationEntityConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationEntityConverter.class);

    //todo check pn NULL
    public LocationEntity toEntity(@NotNull Location location) {
        LOGGER.info("Execute method toEntity in LocationEntityConverter class, got argument location = {} ",
                location);
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    //todo check pn NULL
    public Location toDomain(@NotNull LocationEntity locationEntity) {
        LOGGER.info("Execute method toDomain in LocationEntityConverter class, got argument locationEntity = {} ",
                locationEntity);
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }

    //todo check pn NULL
    public List<Location> toDomain(@NotNull @NotEmpty List<LocationEntity> locationEntities) {
        LOGGER.info("Execute method toDto in LocationDtoConverter class, got argument locationEntities = {} ",
                locationEntities);
        return locationEntities.stream()
                .map(this::toDomain)
                .toList();
    }
}
