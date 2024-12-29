package org.das.event_manager.controller;

import jakarta.validation.Valid;
import org.das.event_manager.converters.LocationDtoConverter;
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
    private final LocationDtoConverter dtoConverter;

    @Autowired
    public LocationController(LocationService locationService, LocationDtoConverter dtoConverter) {
        this.locationService = locationService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping()
    public ResponseEntity<List<LocationDto>> findAll() {
        LOGGER.info("Get request for find all locations");
        return ResponseEntity
                .ok()
                .body(dtoConverter.toDto(locationService.findAll()));
    }

    @PostMapping
    public ResponseEntity<LocationDto> create(
        @RequestBody @Valid LocationDto locationDtoToCreate
    ) {
        LOGGER.info("Post request for create locationDto = {}", locationDtoToCreate);
        Location locationToUpdate = dtoConverter.toDomain(locationDtoToCreate);
        return ResponseEntity.
                status(HttpStatus.CREATED)
               .body(dtoConverter.toDto(locationService.create(locationToUpdate)));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteById(
            @PathVariable("locationId") Long locationId
    ) {
        Location deletedLocation = locationService.deleteById(locationId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(dtoConverter.toDto(deletedLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> findById(
            @PathVariable("locationId") Long locationId
    ) {
       return ResponseEntity
               .status(HttpStatus.FOUND)
               .body(dtoConverter.toDto(locationService.findById(locationId)));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateById(
            @PathVariable Long locationId,
            @RequestBody @Valid LocationDto locationDtoToUpdate
    ) {
        Location updatedLocation = locationService
                .updateById(locationId, dtoConverter.toDomain(locationDtoToUpdate));
        return ResponseEntity.ok().body(dtoConverter.toDto(updatedLocation));
    }
}
