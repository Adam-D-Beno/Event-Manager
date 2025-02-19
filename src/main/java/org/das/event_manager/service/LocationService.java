package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.dto.mappers.LocationEntityMapper;
import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.entity.LocationEntity;
import org.das.event_manager.repository.LocationRepository;
import org.das.event_manager.validation.LocationValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service
@Validated
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;
    private final LocationEntityMapper entityMapper;
    private final LocationValidate locationValidate;

    @Autowired
    public LocationService(
            LocationRepository locationRepository,
            LocationEntityMapper entityMapper,
            LocationValidate locationValidate
    ) {
        this.locationRepository = locationRepository;
        this.entityMapper = entityMapper;
        this.locationValidate = locationValidate;
    }

    public List<Location> findAll() {
        LOGGER.info("Execute method search in LocationService class");
        return entityMapper.toDomain(locationRepository.findAll());
    }

    public Location create(@NotNull Location locationToUpdate) {
        LOGGER.info("Execute method create in LocationService class, got argument locationToUpdate = {}",
                    locationToUpdate);
        locationValidate.validateLocationIdNull(locationToUpdate.id());
        existLocationName(locationToUpdate.name());
        existLocationAddress(locationToUpdate.address());
        return entityMapper.toDomain(locationRepository.save(entityMapper.toEntity(locationToUpdate)));
    }

    public Location deleteById(@NotNull Long locationId) {
        LOGGER.info("Execute method deleteById in LocationService class, got argument locationId = {}",
                locationId);
        LocationEntity foundEntityForDelete = locationRepository.findById(locationId)
                .orElseThrow(() -> {
                   LOGGER.error("No such found location with id={}", locationId);
                   return new EntityNotFoundException("No such found location with id = %s"
                            .formatted(locationId));
                });
        locationRepository.deleteById(locationId);
        return entityMapper.toDomain(foundEntityForDelete);
    }

    public Location findById(@NotNull Long locationId) {
        LOGGER.info("Execute method findById in LocationService class, got argument locationId = {}",
                locationId);
        return locationRepository.findById(locationId)
                .map(entityMapper::toDomain)
                .orElseThrow(() -> {
                   LOGGER.error("No such found location with id={}", locationId);
                   return new EntityNotFoundException("No such found location with id = %s"
                            .formatted(locationId));
                });

    }

    @Transactional
    public Location updateById(@NotNull Long locationId, @NotNull Location location) {
        LOGGER.info("Execute method updateById in LocationService class, got arguments locationId = {}, location = {}",
                locationId, location);
        locationValidate.validateLocationIdNull(location.id());
        LocationEntity foundEntityForUpdate = locationRepository.findById(locationId)
                .orElseThrow(() -> {
                    LOGGER.error("No found location = {} with id = {}",locationId, location);
                    return new EntityNotFoundException("LocationEntity not found by id=%s".formatted(locationId)
                    );
                });
        Integer currentLocationCapacity = foundEntityForUpdate.getCapacity();
        if (location.capacity() < currentLocationCapacity) {
            LOGGER.error("Capacity for update = {} should be greater than the existing capacity = {} " +
                            "for location: id = {}, name = {}",
                    location.capacity(), currentLocationCapacity, locationId, location.name());
            throw new IllegalArgumentException(("Capacity for update = %s should be more "
                    .formatted(location.capacity())
                    + "than the capacity = %s that already exists")
                    .formatted(currentLocationCapacity));
        }
        locationRepository.update(
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
        return entityMapper.toDomain(foundEntityForUpdate);
    }

    public void existLocationAddress(@NotBlank String locationAddress) {
        LOGGER.info("Execute isExistLocationAddress in LocationService class, location address = {}", locationAddress);
        if (locationRepository.existsByAddress(locationAddress)) {
            LOGGER.error("Location address = {} already exist", locationAddress);
            throw new IllegalArgumentException("Location address = %s exist".formatted(locationAddress));
        }
    }

    public void existLocationName(@NotBlank String locationName) {
        LOGGER.info("Execute isExistLocationName in LocationService class, location name = {}", locationName);
        if (locationRepository.existsByName(locationName)) {
            LOGGER.error("location name = {} already exists", locationName);
            throw new IllegalArgumentException("Location: name = %s already exists".formatted(locationName));
        }
    }

    public Integer getCapacity(@NotNull Long locationId) {
        LOGGER.info("Execute getCapacity in LocationService class, location id = {}", locationId);
        return locationRepository.getCapacityById(locationId)
                .orElseThrow(() -> {
                    LOGGER.error("Location capacity is null for locationId: {}", locationId);
                    return new IllegalArgumentException("Location capacity cannot be null");
                });
    }
}
