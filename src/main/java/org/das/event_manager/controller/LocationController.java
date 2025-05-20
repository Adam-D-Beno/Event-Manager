package org.das.event_manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.Location;
import org.das.event_manager.dto.LocationDto;
import org.das.event_manager.dto.mappers.LocationMapper;
import org.das.event_manager.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final LocationMapper LocationMapper;

    @GetMapping()
    public ResponseEntity<List<LocationDto>> findAll() {
        LOGGER.info("Get request for find all locations");

        return ResponseEntity
                .ok()
                .body(LocationMapper.toDto(locationService.findAll()));
    }

    @PostMapping
    public ResponseEntity<LocationDto> create(
        @RequestBody @Valid LocationDto locationDtoToCreate
    ) {
        LOGGER.info("Post request for create locationDto = {}", locationDtoToCreate);

        return ResponseEntity.
                status(HttpStatus.CREATED)
               .body(LocationMapper.toDto(locationService.create(LocationMapper.toDomain(locationDtoToCreate))));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteById(
            @NotNull @PathVariable("locationId") Long locationId
    ) {
        LOGGER.info("Delete request for delete by id = {} location", locationId);

        Location deletedLocation = locationService.deleteById(locationId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(LocationMapper.toDto(deletedLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> findById(
            @NotNull @PathVariable("locationId") Long locationId
    ) {
       LOGGER.info("Get request for find by id = {} location", locationId);

       return ResponseEntity
               .status(HttpStatus.FOUND)
               .body(LocationMapper.toDto(locationService.findById(locationId)));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateById(
            @NotNull @PathVariable("locationId") Long locationId,
            @RequestBody @Valid LocationDto locationDtoToUpdate
    ) {
        LOGGER.info("Put request for update locationDto = {} with id = {}", locationDtoToUpdate, locationId);

        Location updatedLocation = locationService
                .updateById(locationId, LocationMapper.toDomain(locationDtoToUpdate));
        return ResponseEntity.ok().body(LocationMapper.toDto(updatedLocation));
    }
}
