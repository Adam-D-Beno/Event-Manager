package org.das.event_manager.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EventChangeKafkaMessage(
      Long eventId,
      Long modifierById,
      Long ownerEventId,
      EventFieldGeneric<String> name,
      EventFieldGeneric<Integer> maxPlaces,
      EventFieldGeneric<LocalDateTime> date,
      EventFieldGeneric<BigDecimal> cost,
      EventFieldGeneric<Integer> duration,
      EventFieldGeneric<Long> locationId,
      EventFieldGeneric<EventStatus> status,
      List<Long> registrationsOnEvent
) {}