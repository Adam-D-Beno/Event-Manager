package org.das.event_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.*;
import org.das.event_manager.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
        List<Long> waitStartEventsIds =
                eventService.findStartedEventsWithStatus(EventStatus.WAIT_START);
        List<Long> startedEventsIds =
                eventService.findEndedEventsWithStatus(EventStatus.STARTED);
        log.info("Events to start = {}, to end = {}", waitStartEventsIds, startedEventsIds);
        if (!waitStartEventsIds.isEmpty()) {
            log.info("Change Events = {} fron WAIT_START to STARTED", waitStartEventsIds);
            eventService.changeEventStatuses(waitStartEventsIds, EventStatus.STARTED);
            sendEventStatusUpdatesToKafka(waitStartEventsIds, EventStatus.WAIT_START);
        }
        if (!startedEventsIds.isEmpty()) {
            log.info("Change Events = {} fron STARTED to FINISHED", startedEventsIds);
            eventService.changeEventStatuses(startedEventsIds, EventStatus.FINISHED);
            sendEventStatusUpdatesToKafka(waitStartEventsIds, EventStatus.STARTED);
        }
    }

    private void sendEventStatusUpdatesToKafka(
            List<Long> Events,
            EventStatus OldStatus
    ) {
        log.info("Begin send kafka event message for change events statuses on STARTED or FINISHED: events = {}",
                Events);
        Events.forEach(eventId -> {
            Event eventFound = eventService.findById(eventId);
            eventKafkaProducerService.sendEvent(
                    EventChangeKafkaMessage.builder()
                            .eventId(eventFound.id())
                            .ownerEventId(eventFound.ownerId())
                            .status(new EventFieldChange<>(OldStatus, eventFound.status()))
                            .userRegistrationsOnEvent(eventFound.registrations()
                                    .stream()
                                    .map(EventRegistration::id).toList())
                            .build()
            );
        });
    }
}
