package org.das.event_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventKafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(EventKafkaProducerService.class);
    private final KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate;

    public void sendEvent(EventChangeKafkaMessage kafkaEventMessage) {
        log.info("Send kafka event message: event = {}", kafkaEventMessage);
        var res = kafkaTemplate.send("change-event-topic", kafkaEventMessage.ownerEventId(), kafkaEventMessage);
        res.thenAccept(sendRes ->  log.info("Send successful"));
    }
}
