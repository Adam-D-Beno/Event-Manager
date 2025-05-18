package org.das.event_manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.*;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.dto.EventSearchRequestDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.repository.EventRepository;
import org.das.event_manager.service.EventService;
import org.das.event_manager.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final EventKafkaProducerService eventKafkaProducerService;

    @Override
    public Event create(Event eventForCreate) {
        LOGGER.info("Execute method create in EventServiceImpl, event = {}", eventForCreate);
        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();
        Location location = locationService.findById(eventForCreate.locationId());

        if (location.capacity() < eventForCreate.maxPlaces()) {
            LOGGER.error("location capacity < event maxPlaces");
            throw new IllegalArgumentException("Capacity of location is: %s, but maxPlaces is: %s"
                    .formatted(location.capacity(), eventForCreate.maxPlaces()));
        }
        LocalDateTime startDateEvent = eventForCreate.date();
        LocalDateTime endDateEvent = eventForCreate.date().plusMinutes(eventForCreate.duration());
        var isDateForCreateBusy = eventRepository.isDateForCreateEventBusy(
                startDateEvent, endDateEvent, eventForCreate.locationId()
        );
        if (isDateForCreateBusy) {
            LOGGER.error("event on that already exist");
            throw new IllegalArgumentException(("Cannot create event=%s on that date=%s " +
                    "because event on that already exist")
                    .formatted(eventForCreate, eventForCreate.date()));
        }
        EventEntity eventEntity = eventMapper.toEntity(eventForCreate);
        eventEntity.setOwnerId(currentAuthenticatedUser.id());

        EventEntity saved = eventRepository.save(eventEntity);
        return eventMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long eventId) {
        LOGGER.info("Execute method create in EventServiceImpl, event id = {}", eventId);
        Event event = findById(eventId);
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();

        if (event.status() != EventStatus.WAIT_START) {
            LOGGER.error("Cannot modify event in status = {}", event.status());
            throw new IllegalArgumentException("Cannot modify event in status %s".formatted(event.status()));
        }
        if (!event.ownerId().equals(currentAuthUser.id()) && !currentAuthUser.userRole().equals(UserRole.ADMIN)) {
            LOGGER.error("User with login = {} cant modify this event", currentAuthUser.login());
            throw new IllegalArgumentException("User cant modify this event");
        }
        eventRepository.changeEventStatus(event.id(), EventStatus.CANCELLED);
    }

    @Override
    @Transactional
    public Event findById(Long eventId) {
       LOGGER.info("Execute method findById in EventServiceImpl, event id = {}", eventId);
       return eventRepository.findById(eventId)
                .map(eventMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Event with id = %s not found"
                        .formatted(eventId)));
    }

    @Transactional
    @Override
    public List<Event> findAllByIds(List<Long> eventIds) {
        return eventMapper.toDomain(eventRepository.findAllEventsByIds(eventIds));

    }

    @Override
    public Event update(Long eventId, Event eventForUpdate) {
        LOGGER.info("Execute method update in EventServiceImpl, event = {}", eventForUpdate);
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();
        var eventEntity = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("Event with id = %s not found"
                        .formatted(eventId))
        );

        if (!eventEntity.getOwnerId().equals(currentAuthUser.id())
                && !currentAuthUser.userRole().equals(UserRole.ADMIN)) {
            LOGGER.error("User with login = {} cant modify this event", currentAuthUser.login());
            throw new IllegalArgumentException("User cant modify this event");
        }

        if (eventEntity.getStatus() != EventStatus.WAIT_START) {
            LOGGER.error("Cannot modify event in status = {}", eventEntity.getStatus());
            throw new IllegalArgumentException("Cannot modify event in status %s".formatted(eventEntity.getStatus()));
        }

        if (eventForUpdate.maxPlaces() != null || eventForUpdate.locationId() != null) {
            Long locationId = Optional.ofNullable(eventForUpdate.locationId())
                    .orElse(eventEntity.getLocationId());
            Integer maxPlaces = Optional.ofNullable(eventForUpdate.maxPlaces())
                    .orElse(eventEntity.getMaxPlaces());
            Location location = locationService.findById(locationId);

            if (location.capacity() < maxPlaces) {
                LOGGER.error("Capacity of location  less then maxPlaces");
                throw new IllegalArgumentException(
                        "Capacity of location  less then maxPlaces: capacity=%s, maxPlaces=%s"
                        .formatted(location.capacity(), maxPlaces));
            }
        }
        if (eventForUpdate.maxPlaces() != null &&
                eventEntity.getRegistrations().size() > eventForUpdate.maxPlaces()) {
            LOGGER.error("Registration count than more maxPlaces");
            throw new IllegalArgumentException(
                    "Registration count than more maxPlaces = RegCount=%s, MaxPlaces=%s"
                            .formatted(eventEntity.getRegistrations().size(), eventForUpdate.maxPlaces()));
        }
        EventChangeKafkaMessage changeKafkaMessage = getBuildKafkaMessage(eventForUpdate, eventEntity, currentAuthUser);

        Optional.ofNullable(eventForUpdate.name()).ifPresent(eventEntity::setName);
        Optional.ofNullable(eventForUpdate.maxPlaces()).ifPresent(eventEntity::setMaxPlaces);
        Optional.ofNullable(eventForUpdate.date()).ifPresent(eventEntity::setDate);
        Optional.ofNullable(eventForUpdate.cost()).ifPresent(eventEntity::setCost);
        Optional.ofNullable(eventForUpdate.duration()).ifPresent(eventEntity::setDuration);
        Optional.ofNullable(eventForUpdate.locationId()).ifPresent(eventEntity::setLocationId);

        eventRepository.save(eventEntity);
        eventKafkaProducerService.sendEvent(changeKafkaMessage);
        return eventMapper.toDomain(eventEntity);
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
        User currentAuthUser = authenticationService.getCurrentAuthenticatedUser();
        return eventMapper.toDomain(eventRepository.findEventsByOwner_Id((currentAuthUser.id())));
    }

    @Transactional
    @Override
    public List<Long> changeEventStatuses(EventStatus status) {
        if (status == EventStatus.WAIT_START) {
            List<Long> eventsToStarted = findEventsToStarted(EventStatus.WAIT_START);
            if (!eventsToStarted.isEmpty()) {
                eventRepository.changeEventStatuses(eventsToStarted, status);
            }
            return eventsToStarted;
        }
        if (status == EventStatus.STARTED) {
            List<Long> eventsToEnded = findEventsToEnded(EventStatus.STARTED);
            if (!eventsToEnded.isEmpty()) {
                eventRepository.changeEventStatuses(eventsToEnded, status);
            }
            return eventsToEnded;
        }
        return List.of();
    }

    @Override
    public List<Long> findEventsToStarted(EventStatus statusStarted) {
        return eventRepository.findStartedEventsWithStatus(statusStarted);
    }

    @Override
    public List<Long> findEventsToEnded(EventStatus statusEnded) {
        return eventRepository.findEndedEventsWithStatus(statusEnded);
    }

    private EventChangeKafkaMessage getBuildKafkaMessage(
            Event eventForUpdate, EventEntity eventEntity, User currentAuthUser
    ) {
        return EventChangeKafkaMessage.builder()
                .eventId(eventEntity.getId())
                .modifierById(currentAuthUser.id())
                .ownerEventId(eventEntity.getOwnerId())
                .name(new EventFieldGeneric<>(eventEntity.getName(), eventForUpdate.name()))
                .maxPlaces(new EventFieldGeneric<>(eventEntity.getMaxPlaces(), eventForUpdate.maxPlaces()))
                .date(new EventFieldGeneric<>(eventEntity.getDate(), eventForUpdate.date()))
                .cost(new EventFieldGeneric<>(eventEntity.getCost(), eventForUpdate.cost()))
                .duration(new EventFieldGeneric<>(eventEntity.getDuration(), eventForUpdate.duration()))
                .locationId(new EventFieldGeneric<>(eventEntity.getLocationId(), eventForUpdate.locationId()))
                .status(new EventFieldGeneric<>(eventEntity.getStatus(), eventForUpdate.status()))
                .registrationsOnEvent(eventEntity.getRegistrations()
                        .stream()
                        .map(EventRegistrationEntity::getId).toList())
                .build();
    }
}
