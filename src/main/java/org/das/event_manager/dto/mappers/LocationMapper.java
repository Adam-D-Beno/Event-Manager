package org.das.event_manager.dto.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.entity.LocationEntity;
import org.das.event_manager.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationMapper.class);

    public Location toDomain(LocationDto locationDto) {
        LOGGER.info("Execute method toDomain in LocationDtoMapper class, locationDto = {} ",
                locationDto);
        return new Location(
                locationDto.id(),
                locationDto.name(),
                locationDto.address(),
                locationDto.capacity(),
                locationDto.description()
        );
    }

    public LocationDto toDto(Location location) {
        LOGGER.info("Execute method toDto in LocationDtoMapper class, location = {} ",
                location);
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public List<LocationDto> toDto(List<Location> locations) {
        if (locations.isEmpty()) {
            return List.of();
        }
        LOGGER.info("Execute method toDto in LocationDtoMapper class, locations = {} ",
                locations);
        return locations.stream()
                .map(this::toDto)
                .toList();
    }

    public LocationEntity toEntity(Location location) {
        LOGGER.info("Execute method toEntity in LocationEntityMapper class, location = {} ",
                location);
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toDomain(@NotNull LocationEntity locationEntity) {
        LOGGER.info("Execute method toDomain in LocationEntityMapper class, locationEntity = {} ",
                locationEntity);
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }

    public List<Location> toDomain(@NotNull List<LocationEntity> locationEntities) {
        LOGGER.info("Execute method toDto in LocationEntityMapper class, locationEntities = {} ",
                locationEntities);
        return locationEntities.stream()
                .map(this::toDomain)
                .toList();
    }
}
