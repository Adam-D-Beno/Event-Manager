package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.UserRole;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.mappers.EventEntityMapper;
import org.das.event_manager.dto.mappers.UserEntityMapper;
import org.das.event_manager.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public EventService(
            EventRepository eventRepository,
            EventEntityMapper eventEntityMapper,
            UserEntityMapper userEntityMapper,
            LocationService locationService,
            AuthenticationService authenticationService,
            UserService userService
    ) {
        this.eventRepository = eventRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.userEntityMapper = userEntityMapper;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    public Event create(Event event) {
        LOGGER.info("Execute method create in EventService, event = {}", event);
        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();
        checkExistCurrentUser(currentAuthenticatedUser);
        checkExistLocation(event);
        checkMaxPlacesOnLocation(event);
        UserEntity userEntity = userEntityMapper.toEntity(currentAuthenticatedUser);
        EventEntity eventEntity = eventEntityMapper.toEntity(event);
        eventEntity.setOwner(userEntity);
        EventEntity saved = eventRepository.save(eventEntity);
        return eventEntityMapper.toDomain(saved);
    }

    public void deleteById(Long eventId) {
        LOGGER.info("Execute method create in EventService, event id = {}", eventId);
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();

        eventRepository.findById(eventId)
           .filter(eventEntity -> eventEntity.getStatus() == EventStatus.WAIT_START)
           .filter(eventEntity -> currentAuthUser.userRole() == UserRole.ADMIN
                   || eventEntity.getOwner().getId().equals(currentAuthUser.id()))
           .map(eventEntity -> {
                    eventEntity.setStatus(EventStatus.CANCELLED);
                   return eventRepository.save(eventEntity);
                }).orElseThrow(() -> new EntityNotFoundException("Event not found or status is not WAIT_START"));
    }

    public Event findById(Long eventId) {
       return eventRepository.findById(eventId)
                .map(eventEntityMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not find"
                        .formatted(eventId)));
    }

    public Event update(@NotNull Long eventId, Event eventToUpdate) {
        findById(eventId);
        return null;
    }


    private void checkMaxPlacesOnLocation(Event event) {
        LOGGER.info("Checking max places for event: {}", event);
        Integer locationCapacity = locationService.getCapacity(event.locationId());
        if (event.maxPlaces() > locationCapacity) {
            LOGGER.error("Error, Maximum number = {} of places at the event more then location capacity = {} ",
                    event.maxPlaces(), locationCapacity);
            throw new IllegalArgumentException("maxPlaces cannot be more then location maxPlaces");
        }
    }


    private void checkExistCurrentUser(User currentAuthenticatedUser) {
        userService.findById(currentAuthenticatedUser.id());
    }


    private void checkExistLocation(Event event) {
        locationService.findById(event.locationId());
    }

}
