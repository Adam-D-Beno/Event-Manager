package org.das.event_manager.validation;

import org.das.event_manager.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LocationValidate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationValidate.class);

    public void validateNotNull(Long locationId) {
        LOGGER.info("Execute method validateNotNull in LocationValidate class, check locationId");
        if (locationId == null) {
            LOGGER.info("Execute method validateNotNull in LocationValidate class, locationId is NULL");
            throw new IllegalArgumentException("Location id must be not NULL");
        }
    }

    public void validateNotNull(Location location) {
        LOGGER.info("Execute method validateNotNull in LocationValidate class. check location");
        if (location == null) {
            LOGGER.info("Execute method validateNotNull in LocationValidate class, location is NULL");
            throw new IllegalArgumentException("Location must be not NULL");
        }
    }
}
