package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.converters.LocationEntityConverter;
import org.das.event_manager.domain.Location;
import org.das.event_manager.entity.LocationEntity;
import org.das.event_manager.repository.LocationRepository;
import org.das.event_manager.validation.LocationValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service
@Validated
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;
    private final LocationEntityConverter entityConverter;
    private final LocationValidate locationValidate;

    @Autowired
    public LocationService(
            LocationRepository locationRepository,
            LocationEntityConverter entityConverter,
            LocationValidate locationValidate
    ) {
        this.locationRepository = locationRepository;
        this.entityConverter = entityConverter;
        this.locationValidate = locationValidate;
    }

    public List<Location> findAll() {
        LOGGER.info("Execute method findAll in LocationService class");
        return entityConverter.toDomain(locationRepository.findAll());
    }

    public Location create(@NotNull Location locationToUpdate) {
        LOGGER.info("Execute method create in LocationService class, got argument locationToUpdate = {}",
                    locationToUpdate);
        locationValidate.validateNotNull(locationToUpdate);
        LocationEntity savedLocationEntity = locationRepository.save(entityConverter.toEntity(locationToUpdate));
        return entityConverter.toDomain(savedLocationEntity);
    }

    public Location deleteById(@NotNull Long locationId) {
        LOGGER.info("Execute method deleteById in LocationService class, got argument locationId = {}",
                locationId);
        locationValidate.validateNotNull(locationId);
        LocationEntity foundLocationEntityForDelete = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("No such found location with id = %s"
                        .formatted(locationId)));
        locationRepository.deleteById(locationId);
        return entityConverter.toDomain(foundLocationEntityForDelete);
    }

    public Location findById(@NotNull Long locationId) {
        LOGGER.info("Execute method findById in LocationService class, got argument locationId = {}",
                locationId);
        locationValidate.validateNotNull(locationId);
        return locationRepository.findById(locationId)
                .map(entityConverter::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("No such found location woth id = %s"
                        .formatted(locationId)));
    }

    public Location updateById(@NotNull Long locationId, @NotNull Location location) {
        LOGGER.info("Execute method updateById in LocationService class, got arguments locationId = {}, location = {}",
                locationId, location);
        locationValidate.validateNotNull(locationId);
        locationValidate.validateNotNull(location);
        if (!locationRepository.existsById(locationId)) {
            throw new EntityNotFoundException(
                    "No found book by id=%s".formatted(locationId)
            );
        }
        Integer foundCapacity = locationRepository.getCapacityById(locationId);
        if (location.capacity() < foundCapacity) {
            throw new IllegalArgumentException(("Capacity for update %s should be more " +
                    "than the capacity that already exists %s")
                    .formatted(location.capacity(), foundCapacity));
        }
        LocationEntity updatedLocationEntity = locationRepository.update(
                locationId,
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
        return entityConverter.toDomain(updatedLocationEntity);
    }

    //todo which argument need check
    public void isExistLocationAddress(String address) {
        LOGGER.info("Execute isExistLocationAddress in LocationService class, address = {}", address);
        if (locationRepository.existsByName(address)) {
            LOGGER.info("Location address = {} exist", address);
            throw new IllegalArgumentException("Location address = %s exist".formatted(address));
        }
    }
}
