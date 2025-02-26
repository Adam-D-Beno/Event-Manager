package org.das.event_manager.dto.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.EventRegistration;
import org.das.event_manager.domain.entity.EventRegistrationEntity;

import java.util.List;

public interface RegistrationMapper {

    EventRegistration toDomain(@NotNull EventRegistrationEntity registrationEntity);
    List<EventRegistration> toDomain(@NotNull List<EventRegistrationEntity> registrationEntities);
}
