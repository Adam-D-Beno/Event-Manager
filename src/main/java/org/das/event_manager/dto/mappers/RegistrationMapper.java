package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.EventRegistration;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import java.util.List;

public interface RegistrationMapper {

    EventRegistration toDomain(EventRegistrationEntity registrationEntity);
    List<EventRegistration> toDomain(List<EventRegistrationEntity> registrationEntities);
    List<EventRegistrationEntity> toEntity(List<EventRegistration> eventRegistrations);
}
