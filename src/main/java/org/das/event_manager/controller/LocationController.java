package org.das.event_manager.controller;

import org.das.event_manager.dto.LocationDto;
import org.springframework.http.ResponseEntity;

public interface LocationController {

    ResponseEntity<LocationDto> create(LocationDto locationDtoToCreate);
    ResponseEntity<LocationDto> deleteById(Long locationId);
    ResponseEntity<LocationDto> findById(Long locationId);
    ResponseEntity<LocationDto> updateById(Long locationId, LocationDto locationDtoToUpdate);
}
