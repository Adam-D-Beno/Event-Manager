package org.das.event_manager.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LocationValidate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationValidate.class);


    public void validateLocationIdNull(Long locationId) {
        LOGGER.info("Execute method isLocationIdNull in LocationValidate class, checking locationId = {}"
                , locationId);

        if (locationId != null) {
            LOGGER.info("Cannot creation Location with provided id. id = {} must be empty", locationId);

            throw new IllegalArgumentException("Cannot creation Location with provided id. id must be empty");
        }
    }
}
