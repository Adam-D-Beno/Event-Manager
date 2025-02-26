package org.das.event_manager.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Location;

import java.util.List;

public interface LocationService {

     List<Location> findAll();
     Location create(Location locationToUpdate);
     Location deleteById(Long locationId);
     Location findById(Long locationId);
     Location updateById(Long locationId, Location location);
     Integer getCapacity(Long locationId);
     void existLocationAddress(String locationAddress);
     void existLocationName(String locationName);
}
