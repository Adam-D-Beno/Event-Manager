package org.das.event_manager.validation;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.User;
import org.das.event_manager.service.LocationService;
import org.das.event_manager.service.UserService;
import org.das.event_manager.service.impl.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EventValidate {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventValidate.class);
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final LocationService locationService;

    public EventValidate(
            AuthenticationService authenticationService,
            UserService userService,
            LocationService locationService
           ) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.locationService = locationService;
    }

    @Deprecated
    public void checkCostMoreThenZero(BigDecimal eventCost) {
        LOGGER.info("Execute method checkCostMoreThenZero, cost = {}", eventCost);

        if (eventCost != null && eventCost.compareTo(BigDecimal.ZERO) <= 0) {
            LOGGER.error("Cost must be more then zero or negative= {}", eventCost);
            throw new IllegalArgumentException("Cost = %s for update must be more then zero"
                    .formatted(eventCost));
        }
    }


    @Deprecated
    public void checkMaxPlacesMoreCurrentMaxPlaces(Event event, Event eventToUpdate) {
        LOGGER.info("Execute method checkMaxPlacesMoreCurrentMaxPlaces event id = {}, cost = {}",
                event.id(), event.cost());

        Integer maxPlacesToUpdate = eventToUpdate.maxPlaces();
        Integer currentMaxPlaces = event.maxPlaces();
        if (maxPlacesToUpdate == null || currentMaxPlaces == null) {
            return;
        }
        if (maxPlacesToUpdate < currentMaxPlaces) {
            LOGGER.error("Max places for update = {}, cannot be then max places already exist = {}",
                    maxPlacesToUpdate, currentMaxPlaces);
            throw new IllegalArgumentException("Max places for update = %s must be more then current max places = %s"
                    .formatted(maxPlacesToUpdate, currentMaxPlaces));
        }
    }

    @Deprecated
    public void checkDurationLessThenThirty(Integer eventDuration) {
        LOGGER.info("Execute method checkDurationLessThenThirtyThrow, duration = {}",
                eventDuration);

        if (eventDuration != null && eventDuration < 30 ) {
            LOGGER.error("Duration Less Then Thirty = {}", eventDuration);
            throw new IllegalArgumentException("Duration = %s for update must be more 30"
                    .formatted(eventDuration));
        }
    }

    @Deprecated
    public void checkMaxPlacesMoreThenOnLocation(Integer eventMaxPlaces, Integer locationCapacity) {
        LOGGER.info("Execute method checkMaxPlacesMoreThenOnLocation, max places = {}, locationCapacity = {}",
                eventMaxPlaces, locationCapacity);

        if (eventMaxPlaces == null || locationCapacity == null) {
            return;
        }
        if (eventMaxPlaces > locationCapacity) {
            LOGGER.error("Max places = {} at the event more then location capacity = {} ",
                    eventMaxPlaces, locationCapacity);
            throw new IllegalArgumentException("maxPlaces = %s cannot be more then location capacity = %s"
                    .formatted(eventMaxPlaces, locationCapacity));
        }
    }

    @Deprecated
    public void checkExistUser(User currentAuthenticatedUser) {
        LOGGER.info("Execute method checkExistUser, user = {}", currentAuthenticatedUser);
        userService.findById(currentAuthenticatedUser.id());
    }
}
