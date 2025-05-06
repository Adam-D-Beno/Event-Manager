package org.das.event_manager.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventChangeKafkaMessage(
      Long eventId,
      Long userEventChangedId,
      Long ownerEventId,
      EventFieldChange<String> name,
      EventFieldChange<Integer> MaxPlaces,
      EventFieldChange<LocalDateTime> date,
      EventFieldChange<BigDecimal> cost,
      EventFieldChange<Integer> duration,
      EventFieldChange<Long> locationId,
      EventFieldChange<EventStatus> status,
      List<Long> userRegistrationsOnEvent
) {}
