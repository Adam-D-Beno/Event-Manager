package org.das.event_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.EventStatus;
import org.das.event_manager.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerService {

    private final static Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final EventRepository eventRepository;


    @Scheduled(cron = "${event.stats.cron}")
    public List<Long> updateEventStatuses() {
        log.info("EventStatus Scheduled Updater started");
        List<Long> waitStartEvents =
                eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        List<Long> endedEventsWithStatus =
                eventRepository.findEndedEventsWithStatus(EventStatus.STARTED);
        log.warn("Events for start = {}, end = {}", waitStartEvents, endedEventsWithStatus);

        waitStartEvents
                .forEach(eventId -> eventRepository.changeEventStatus(eventId, EventStatus.STARTED));
        endedEventsWithStatus
                .forEach(eventId -> eventRepository.changeEventStatus(eventId, EventStatus.FINISHED));
        return endedEventsWithStatus;
    }
}
