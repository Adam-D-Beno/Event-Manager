package org.das.event_manager.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Location;

import java.util.List;

public interface LocationService {

     List<Location> findAll();
     Location create(@NotNull Location locationToUpdate);
     Location deleteById(@NotNull Long locationId);
     Location findById(@NotNull Long locationId);
     Location updateById(@NotNull Long locationId, @NotNull Location location);
     Integer getCapacity(@NotNull Long locationId);
     void existLocationAddress(@NotBlank String locationAddress);
     void existLocationName(@NotBlank String locationName);
}
