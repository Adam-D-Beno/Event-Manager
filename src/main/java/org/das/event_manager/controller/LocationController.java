package org.das.event_manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.dto.mappers.LocationDtoMapper;
import org.das.event_manager.domain.Location;
import org.das.event_manager.dto.LocationDto;
import org.das.event_manager.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final LocationDtoMapper dtoMapper;

    @Autowired
    public LocationController(LocationService locationService, LocationDtoMapper dtoMapper) {
        this.locationService = locationService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping()
    public ResponseEntity<List<LocationDto>> findAll() {
        LOGGER.info("Get request for find all locations");
        return ResponseEntity
                .ok()
                .body(dtoMapper.toDto(locationService.findAll()));
    }

    @PostMapping
    public ResponseEntity<LocationDto> create(
        @RequestBody @Valid LocationDto locationDtoToCreate
    ) {
        LOGGER.info("Post request for create locationDto = {}", locationDtoToCreate);
        return ResponseEntity.
                status(HttpStatus.CREATED)
               .body(dtoMapper.toDto(locationService.create(dtoMapper.toDomain(locationDtoToCreate))));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteById(
            @NotNull @PathVariable("locationId") Long locationId
    ) {
        LOGGER.info("Delete request for delete by id = {} location", locationId);
        Location deletedLocation = locationService.deleteById(locationId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(dtoMapper.toDto(deletedLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> findById(
            @NotNull @PathVariable("locationId") Long locationId
    ) {
       LOGGER.info("Get request for find by id = {} location", locationId);
       return ResponseEntity
               .status(HttpStatus.FOUND)
               .body(dtoMapper.toDto(locationService.findById(locationId)));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateById(
            @NotNull @PathVariable("locationId") Long locationId,
            @RequestBody @Valid LocationDto locationDtoToUpdate
    ) {
        LOGGER.info("Put request for update locationDto = {} with id = {}", locationDtoToUpdate, locationId);
        Location updatedLocation = locationService
                .updateById(locationId, dtoMapper.toDomain(locationDtoToUpdate));
        return ResponseEntity.ok().body(dtoMapper.toDto(updatedLocation));
    }
}
