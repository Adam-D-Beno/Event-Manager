package org.das.event_manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.*;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.dto.mappers.LocationMapper;
import org.das.event_manager.dto.mappers.UserMapper;
import org.das.event_manager.repository.EventRepository;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.EventService;
import org.das.event_manager.service.LocationService;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Validated
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public EventServiceImpl(
            EventRepository eventRepository,
            EventMapper eventMapper,
            UserMapper userMapper,
            LocationMapper locationMapper,
            LocationService locationService,
            AuthenticationService authenticationService,
            UserService userService
    ) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    public Event create(@NotNull Event event) {
        LOGGER.info("Execute method create in EventServiceImpl, event = {}", event);

        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        checkExistUser(currentAuthenticatedUser);
        checkExistLocation(event);
        checkMaxPlacesMoreThenOnLocation(event);

        UserEntity userEntity = userMapper.toEntity(currentAuthenticatedUser);
        userEntity.setId(currentAuthenticatedUser.id());
        EventEntity eventEntity = eventMapper.toEntity(event);

        eventEntity.setOwner(userEntity);
        EventEntity saved = eventRepository.save(eventEntity);
        return eventMapper.toDomain(saved);
    }

    @Override
    public void deleteById(@NotNull Long eventId) {
        LOGGER.info("Execute method create in EventServiceImpl, event id = {}", eventId);

        checkStatusEvent(eventId);
        checkCurrentUserCanModify(eventId);

        eventRepository.findById(eventId)
           .map(eventEntity -> {
                    eventEntity.setStatus(EventStatus.CANCELLED);
                   return eventRepository.save(eventEntity);
                }).orElseThrow(() -> new EntityNotFoundException("Event not found or status is not WAIT_START"));
    }

    @Override
    public Event findById(@NotNull Long eventId) {
       LOGGER.info("Execute method findById in EventServiceImpl, event id = {}", eventId);
       return eventRepository.findById(eventId)
                .map(eventMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not find"
                        .formatted(eventId)));
    }

    @Transactional
    @Override
    public Event update(@NotNull Long eventId, @NotNull Event eventToUpdate) {
        checkDurationLessThenThirtyThrow(eventToUpdate);
        checkMaxPlacesMoreCurrentMaxPlaces(eventId, eventToUpdate);
        checkDatePastTime(eventToUpdate);
        checkCostMoreThenZero(eventToUpdate);
        checkCurrentUserCanModify(eventId);
        checkStatusEvent(eventId);

        Location location = locationService.findById(eventToUpdate.locationId());
        EventEntity updated = eventRepository.findById(eventId)
                .map(eventEntity -> {
                    eventEntity.setId(eventId);
                    eventEntity.setName(eventToUpdate.name());
                    eventEntity.setMaxPlaces(eventToUpdate.maxPlaces());
                    eventEntity.setDate(eventToUpdate.date());
                    eventEntity.setCost(eventToUpdate.cost());
                    eventEntity.setDuration(eventToUpdate.duration());
                    eventEntity.setLocation(locationMapper.toEntity(location));
                    return eventRepository.save(eventEntity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not find"
                        .formatted(eventId)));
        return eventMapper.toDomain(updated);
    }

    @Override
    public List<Event> search(EventSearchRequestDto eventSearchRequestDto) {
        LOGGER.info("Execute method search in EventServiceImpl, eventSearchRequestDto = {}"
                , eventSearchRequestDto);

        List<EventEntity> searched = eventRepository.search(
                eventSearchRequestDto.name(),
                eventSearchRequestDto.placesMin(),
                eventSearchRequestDto.placesMax(),
                eventSearchRequestDto.dateStartAfter(),
                eventSearchRequestDto.dateStartBefore(),
                eventSearchRequestDto.costMin(),
                eventSearchRequestDto.costMax(),
                eventSearchRequestDto.durationMin(),
                eventSearchRequestDto.durationMax(),
                eventSearchRequestDto.locationId(),
                eventSearchRequestDto.status()
        );
        return eventMapper.toDomain(searched);
    }

    @Override
    public List<Event> findAllEventsCreationByOwner() {
        LOGGER.info("Execute method findAllEventsCreationByOwner in EventServiceImpl, eventSearchRequestDto");

        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        return eventMapper.toDomain(eventRepository.findEventsByOwner_Id((currentAuthUser.id())));
    }


    private void checkDatePastTime(@NotNull Event event) {
        LOGGER.info("Execute method checkDatePastTime in EventServiceImpl, event date = {}", event.date());

        if (event.date() != null && event.date().isBefore(ZonedDateTime.now())) {
            LOGGER.error("Date cannot be a past time = {}", event.date());

            throw new IllegalArgumentException("Data for update = %s must be after current date event"
                    .formatted(event.date()));
        }
    }

    private void checkCostMoreThenZero(@NotNull Event event) {
        LOGGER.info("Execute method checkCostMoreThenZero in EventServiceImpl, cost = {}", event.cost());

        if (event.cost() != null && event.cost().compareTo(BigDecimal.ZERO) <= 0) {
            LOGGER.error("Cost must be more then zero or negative= {}", event.cost());

            throw new IllegalArgumentException("Cost = %s for update must be more then zero"
                    .formatted(event.cost()));
        }
    }

    private void checkCurrentUserCanModify(@NotNull Long eventId) {
        LOGGER.info("Execute method checkCurrentUserCanModify in EventServiceImpl,event id = {}",
                eventId);

        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        Event event = findById(eventId);
        if (!event.ownerId().equals(currentAuthUser.id()) && !(currentAuthUser.userRole() == UserRole.ADMIN)) {
            LOGGER.error("User with login = {} cant modify this event", currentAuthUser.login());

            throw new IllegalArgumentException("User cant modify this event");
        }
    }

    private void checkMaxPlacesMoreCurrentMaxPlaces(@NotNull Long eventId, Event event) {
        LOGGER.info("Execute method checkMaxPlacesMoreCurrentMaxPlaces in EventServiceImpl,event id = {}, cost = {}",
                 eventId, event.cost());

        Integer maxPlacesToUpdate = event.maxPlaces();
        Integer currentMaxPlaces =  findById(eventId).maxPlaces();
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

    private void checkDurationLessThenThirtyThrow(@NotNull Event event) {
        LOGGER.info("Execute method checkDurationLessThenThirtyThrow in EventServiceImpl, duration = {}",
                event.duration());

        if (event.duration() != null && event.duration() < 30 ) {
            LOGGER.error("Duration Less Then Thirty = {}", event.duration());

            throw new IllegalArgumentException("Duration = %s for update must be more 30"
                    .formatted(event.duration()));
        }
    }

    private void checkMaxPlacesMoreThenOnLocation(@NotNull Event event) {
        LOGGER.info("Execute method checkMaxPlacesMoreThenOnLocation in EventServiceImpl, max places = {}",
                event.maxPlaces());

        Integer locationCapacity = locationService.getCapacity(event.locationId());

        if (event.maxPlaces() == null || locationCapacity == null) {
            return;
        }
        if (event.maxPlaces() > locationCapacity) {
            LOGGER.error("Max places = {} at the event more then location capacity = {} ",
                    event.maxPlaces(), locationCapacity);

            throw new IllegalArgumentException("maxPlaces =%s cannot be more then location maxPlaces =%s"
                    .formatted(event.maxPlaces(), locationCapacity));
        }
    }

    private void checkExistUser(@NotNull User currentAuthenticatedUser) {
        LOGGER.info("Execute method checkExistUser in EventServiceImpl, user = {}", currentAuthenticatedUser);

        userService.findById(currentAuthenticatedUser.id());
    }

    private void checkExistLocation(@NotNull Event event) {
        LOGGER.info("Execute method checkExistLocation in EventServiceImpl, user = {}", event.locationId());

        locationService.findById(event.locationId());
    }

    private void checkStatusEvent(@NotNull Long eventId) {
        LOGGER.info("Execute method checkStatusEvent in EventServiceImpl, event id= {}", eventId);

        Event event = findById(eventId);
        if (event.status() != null && event.status() == EventStatus.STARTED) {
            LOGGER.error("Cannot event has status = {}", event.status());

            throw new IllegalArgumentException("Event has status %s".formatted(event.status()));
        }
    }

}
