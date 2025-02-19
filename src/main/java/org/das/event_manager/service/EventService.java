package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import org.das.event_manager.domain.*;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.RegistrationEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.das.event_manager.dto.mappers.EventEntityMapper;
import org.das.event_manager.dto.mappers.LocationEntityMapper;
import org.das.event_manager.dto.mappers.UserEntityMapper;
import org.das.event_manager.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final LocationEntityMapper locationEntityMapper;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RegistrationOnEventService registrationOnEventService;

    public EventService(
            EventRepository eventRepository,
            EventEntityMapper eventEntityMapper,
            UserEntityMapper userEntityMapper,
            LocationEntityMapper locationEntityMapper,
            LocationService locationService,
            AuthenticationService authenticationService,
            UserService userService,
            RegistrationOnEventService registrationOnEventService
    ) {
        this.eventRepository = eventRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.userEntityMapper = userEntityMapper;
        this.locationEntityMapper = locationEntityMapper;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.registrationOnEventService = registrationOnEventService;
    }


    public Event create(Event event) {
        LOGGER.info("Execute method create in EventService, event = {}", event);
        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        checkExistUser(currentAuthenticatedUser);
        checkExistLocation(event);
        checkMaxPlacesMoreThenOnLocation(event);
        UserEntity userEntity = userEntityMapper.toEntity(currentAuthenticatedUser);
        EventEntity eventEntity = eventEntityMapper.toEntity(event);
        eventEntity.setOwner(userEntity);
        EventEntity saved = eventRepository.save(eventEntity);
        return eventEntityMapper.toDomain(saved);
    }

    public void deleteById(Long eventId) {
        LOGGER.info("Execute method create in EventService, event id = {}", eventId);
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        //todo need replace
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
       LOGGER.info("Execute method findById in EventService, event id = {}", eventId);
       return eventRepository.findById(eventId)
                .map(eventEntityMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not find"
                        .formatted(eventId)));
    }

    public Event update(Long eventId, Event eventToUpdate) {
        checkDurationLessThenThirtyThrow(eventToUpdate);
        checkMaxPlacesMoreCurrentMaxPlaces(eventId, eventToUpdate);
        checkDatePastTime(eventToUpdate);
        checkCostMoreThenZero(eventToUpdate);

        Location location = locationService.findById(eventToUpdate.locationId());
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        EventEntity updated = eventRepository.findById(eventId)
                .filter(eventEntity -> eventEntity.getStatus() == EventStatus.WAIT_START)
                .filter(eventEntity -> currentAuthUser.userRole() == UserRole.ADMIN
                        || eventEntity.getOwner().getId().equals(currentAuthUser.id()))
                .map(eventEntity -> {
                    eventEntity.setId(eventId);
                    eventEntity.setName(eventToUpdate.name());
                    eventEntity.setMaxPlaces(eventToUpdate.maxPlaces());
                    eventEntity.setDate(eventToUpdate.date());
                    eventEntity.setCost(eventToUpdate.cost());
                    eventEntity.setDuration(eventToUpdate.duration());
                    eventEntity.setLocation(locationEntityMapper.toEntity(location));
                    return eventRepository.save(eventEntity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not find"
                        .formatted(eventId)));
        return eventEntityMapper.toDomain(updated);
    }

    public List<Event> search(EventSearchRequestDto eventSearchRequestDto) {
        LOGGER.info("Execute method search in EventService, eventSearchRequestDto = {}", eventSearchRequestDto);
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
        return eventEntityMapper.toDomain(searched);
    }

    public List<Event> findAllEventsCreationByOwner() {
        LOGGER.info("Execute method findAllEventsCreationByOwner in EventService, eventSearchRequestDto");
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        return eventEntityMapper.toDomain(eventRepository.findEventsByOwner_Id((currentAuthUser.id())));
    }

    public void registrationOnEvent(Long eventId) {
        LOGGER.info("Execute method search in EventService, event Id = {}", eventId);
        Event eventFound = findById(eventId);
        checkStatusEvent(eventFound);
        //todo need check exist user or not
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        registrationOnEventService.registerUserOnEvent(eventFound, currentAuthUser);
    }


    public List<Event> findAllEventByUserRegistration() {
        LOGGER.info("Execute method findAllEventByUserRegistration in EventService");
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        List<Registration> userRegistration = registrationOnEventService.findAllByUserRegistration(currentAuthUser);
        return userRegistration.stream()
                .map(Registration::event)
                .toList();

    }

    private void checkStatusEvent(Event event) {
        LOGGER.info("Execute method checkStatusEvent in EventService, event = {}", event);
        if (event.status() == EventStatus.CANCELLED || event.status() == EventStatus.FINISHED) {
            LOGGER.error("Cannot registration event has status = {}",
                    event.status());
            throw new IllegalArgumentException("Event has status %s".formatted(event.status()));
        }
    }

    private void checkDatePastTime(Event event) {
        LOGGER.info("Execute method checkDatePastTime in EventService, event date = {}", event.date());
        if (event.date().isAfter(ZonedDateTime.now())) {
            LOGGER.error("Date cannot be a past time = {}", event.date());
            throw new IllegalArgumentException("Data for update = %s must be after current date event"
                    .formatted(event.date()));
        }
    }

    private void checkCostMoreThenZero(Event event) {
        LOGGER.info("Execute method checkCostMoreThenZero in EventService, cost = {}", event.cost());
        if (event.cost().compareTo(BigDecimal.ZERO) <= 0) {
            LOGGER.error("Cost must be more then zero or negative= {}", event.cost());
            throw new IllegalArgumentException("Cost = %s for update must be more then zero"
                    .formatted(event.cost()));
        }
    }

    private void checkMaxPlacesMoreCurrentMaxPlaces(Long eventId, Event event) {
        LOGGER.info("Execute method checkMaxPlacesMoreCurrentMaxPlaces in EventService,event id = {}, cost = {}",
                 eventId, event.cost());
        Integer maxPlacesToUpdate = event.maxPlaces();
         int currentMaxPlaces =  findById(eventId).maxPlaces();
         if (maxPlacesToUpdate < currentMaxPlaces) {
             LOGGER.error("Max places for update = {}, cannot be then max places already exist = {}",
                     maxPlacesToUpdate, currentMaxPlaces);
             throw new IllegalArgumentException("Max places for update = %s must be more then current max places = %s"
                     .formatted(maxPlacesToUpdate, currentMaxPlaces));
         }
    }

    private void checkDurationLessThenThirtyThrow(Event event) {
        LOGGER.info("Execute method checkDurationLessThenThirtyThrow in EventService, duration = {}",
                event.duration());
        if (event.duration() < 30 ) {
            LOGGER.error("Duration Less Then Thirty = {}", event.duration());
            throw new IllegalArgumentException("Duration = %s for update must be more 30".formatted(event.duration()));
        }
    }

    private void checkMaxPlacesMoreThenOnLocation(Event event) {
        LOGGER.info("Execute method checkMaxPlacesMoreThenOnLocation in EventService, max places = {}",
                event.maxPlaces());
        Integer locationCapacity = locationService.getCapacity(event.locationId());
        if (event.maxPlaces() > locationCapacity) {
            LOGGER.error("Max places = {} at the event more then location capacity = {} ",
                    event.maxPlaces(), locationCapacity);
            throw new IllegalArgumentException("maxPlaces =%s cannot be more then location maxPlaces =%s"
                    .formatted(event.maxPlaces(), locationCapacity));
        }
    }

    private void checkExistUser(User currentAuthenticatedUser) {
        LOGGER.info("Execute method checkExistUser in EventService, user = {}", currentAuthenticatedUser);
        userService.findById(currentAuthenticatedUser.id());
    }

    private void checkExistLocation(Event event) {
        LOGGER.info("Execute method checkExistLocation in EventService, user = {}", event.locationId());
        locationService.findById(event.locationId());
    }

}
