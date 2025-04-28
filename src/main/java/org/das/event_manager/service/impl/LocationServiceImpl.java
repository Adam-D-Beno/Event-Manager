package org.das.event_manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.das.event_manager.domain.Location;
import org.das.event_manager.domain.entity.LocationEntity;
import org.das.event_manager.dto.mappers.LocationMapper;
import org.das.event_manager.repository.LocationRepository;
import org.das.event_manager.service.LocationService;
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
public class LocationServiceImpl implements LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);
    private final LocationRepository locationRepository;
    private final LocationMapper entityMapper;
    private final LocationValidate locationValidate;

    @Autowired
    public LocationServiceImpl(
            LocationRepository locationRepository,
            LocationMapper entityMapper,
            LocationValidate locationValidate
    ) {
        this.locationRepository = locationRepository;
        this.entityMapper = entityMapper;
        this.locationValidate = locationValidate;
    }

    @Override
    public List<Location> findAll() {
        LOGGER.info("Execute method search in LocationService class");
        return entityMapper.toDomain(locationRepository.findAll());
    }

    @Override
    public Location create(Location locationToUpdate) {
        LOGGER.info("Execute method create in LocationService class, got argument locationToUpdate = {}",
                    locationToUpdate);
        locationValidate.validateLocationIdNull(locationToUpdate.id());
        existLocationName(locationToUpdate.name());
        existLocationAddress(locationToUpdate.address());
        return entityMapper.toDomain(locationRepository.save(entityMapper.toEntity(locationToUpdate)));
    }

    @Override
    public Location deleteById(Long locationId) {
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

    @Override
    public Location findById(Long locationId) {
        LOGGER.info("Execute method findById in LocationService class, got argument locationId = {}",
                locationId);

        return locationRepository.findById(locationId)
                .map(entityMapper::toDomain)
                .orElseThrow(() -> {
                   LOGGER.error("No found location with id={}", locationId);

                   return new EntityNotFoundException("No such found location with id = %s"
                            .formatted(locationId));
                });

    }

    @Transactional
    @Override
    public Location updateById(Long locationId, Location location) {
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

    private void existLocationAddress(String locationAddress) {
        LOGGER.info("Execute isExistLocationAddress in LocationService class, location address = {}"
                , locationAddress);

        if (locationRepository.existsByAddress(locationAddress)) {
            LOGGER.error("Location address = {} already exist", locationAddress);

            throw new IllegalArgumentException("Location address = %s exist".formatted(locationAddress));
        }
    }

    private void existLocationName(String locationName) {
        LOGGER.info("Execute isExistLocationName in LocationService class, location name = {}", locationName);

        if (locationRepository.existsByName(locationName)) {
            LOGGER.error("location name = {} already exists", locationName);

            throw new IllegalArgumentException("Location: name = %s already exists".formatted(locationName));
        }
    }
}
