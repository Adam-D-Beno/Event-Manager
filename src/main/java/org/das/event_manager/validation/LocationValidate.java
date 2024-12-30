package org.das.event_manager.validation;

import org.das.event_manager.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LocationValidate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationValidate.class);

//    public void validateLocationIdNotNull(Long locationId) {
//        LOGGER.info("Execute method validateNotNull in LocationValidate class, checking locationId");
//        if (locationId == null) {
//            LOGGER.info("Cannot creation Location with id. id must be not empty");
//            throw new IllegalArgumentException("Cannot creation Location with id. id must be not empty");
//        }
//    }
//
//    public void validateLocationNotNull(Location location) {
//        LOGGER.info("Execute method validateNotNull in LocationValidate class. checking location");
//        if (location == null) {
//            LOGGER.info("Execute method validateNotNull in LocationValidate class, location is NULL");
//            throw new IllegalArgumentException("Location must be not NULL");
//        }
//    }

    public void validateLocationIdNull(Long locationId) {
        LOGGER.info("Execute method isLocationIdNull in LocationValidate class, checking locationId");
        if (locationId == null) {
            LOGGER.info("Cannot creation Location with provided id. id must be empty");
            throw new IllegalArgumentException("Cannot creation Location with provided id. id must be empty");
        }
    }
}
