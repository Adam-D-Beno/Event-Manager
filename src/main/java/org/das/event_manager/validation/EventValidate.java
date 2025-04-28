package org.das.event_manager.validation;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.UserRole;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.LocationService;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public void checkDatePastTime(LocalDateTime eventDate) {
        LOGGER.info("Execute method checkDatePastTime, event date = {}", eventDate);

        if (eventDate != null && eventDate.isBefore(LocalDateTime.now())) {
            LOGGER.error("Date cannot be a past time = {}", eventDate);

            throw new IllegalArgumentException("Data for update = %s must be after current date event"
                    .formatted(eventDate));
        }
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

    public void checkCurrentUserCanModify(Long eventOwnerId) {
        LOGGER.info("Execute method checkCurrentUserCanModify eventOwnerId = {}",
                eventOwnerId);

        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();
        if (!eventOwnerId.equals(currentAuthUser.id()) && !currentAuthUser.userRole().equals(UserRole.ADMIN)) {
            LOGGER.error("User with login = {} cant modify this event", currentAuthUser.login());

            throw new IllegalArgumentException("User cant modify this event");
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

    @Deprecated
    public void checkExistLocation(Long locationId) {
        LOGGER.info("Execute method checkExistLocation , location id = {}", locationId);

        locationService.findById(locationId);
    }

    public void checkStatusEvent(EventStatus status) {
        LOGGER.info("Execute method checkStatusEvent, event status = {}", status);

        if (status != EventStatus.WAIT_START) {
            LOGGER.error("Cannot modify event in status = {}", status);

            throw new IllegalArgumentException("Cannot modify event in status %s".formatted(status));
        }
    }
}
