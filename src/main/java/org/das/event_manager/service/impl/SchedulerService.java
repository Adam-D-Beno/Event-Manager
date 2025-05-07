package org.das.event_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.*;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.repository.EventRepository;
import org.das.event_manager.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerService {

    private final static Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final EventService eventService;
    private final EventKafkaProducerService eventKafkaProducerService;

    @Transactional
    @Scheduled(cron = "${event.stats.cron}")
    public void updateEventStatuses() {
        log.info("EventStatus Scheduled Updater started");
        List<Long> waitStartEvents =
                eventService.findStartedEventsWithStatus(EventStatus.WAIT_START);
        List<Long> endedEventsWithStatus =
                eventService.findEndedEventsWithStatus(EventStatus.STARTED);
        log.info("Events for start = {}, end = {}", waitStartEvents, endedEventsWithStatus);
        if (!waitStartEvents.isEmpty()) {
            eventService.changeEventStatuses(waitStartEvents, EventStatus.STARTED);
            waitStartEvents.forEach(eventId -> {
                Event eventFound = eventService.findById(eventId);
                eventKafkaProducerService.sendEvent(
                        EventChangeKafkaMessage.builder()
                                .eventId(eventFound.id())
                                .ownerEventId(eventFound.ownerId())
                                .status(new EventFieldChange<>(EventStatus.WAIT_START, eventFound.status()))
                                .userRegistrationsOnEvent(eventFound.registrations()
                                        .stream()
                                        .map(EventRegistration::id).toList())
                                .build()
                );
            });
        }
        if (!endedEventsWithStatus.isEmpty()) {
            eventService.changeEventStatuses(endedEventsWithStatus, EventStatus.FINISHED);
            endedEventsWithStatus.forEach(eventId -> {
                Event eventFound = eventService.findById(eventId);
                eventKafkaProducerService.sendEvent(
                        EventChangeKafkaMessage.builder()
                                .eventId(eventFound.id())
                                .ownerEventId(eventFound.ownerId())
                                .status(new EventFieldChange<>(EventStatus.FINISHED, eventFound.status()))
                                .userRegistrationsOnEvent(eventFound.registrations()
                                        .stream()
                                        .map(EventRegistration::id).toList())
                                .build()
                );
            });
        }
    }
}
