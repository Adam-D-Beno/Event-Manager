package org.das.event_manager.dto.mappers.impl;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.EventRegistration;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.dto.mappers.RegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
public class RegistrationMapperImpl implements RegistrationMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationMapperImpl.class);
    private final EventMapperImpl eventMapperImpl;
    private final UserMapperImpl userMapperImpl;

    public RegistrationMapperImpl(
            EventMapperImpl eventMapperImpl,
            UserMapperImpl userMapperImpl
    ) {
        this.eventMapperImpl = eventMapperImpl;
        this.userMapperImpl = userMapperImpl;
    }

   @Override
   public EventRegistration toDomain(@NotNull EventRegistrationEntity registrationEntity) {
       LOGGER.info("Execute method toDomain in RegistrationEntityMapper,registrationEntity = {}"
               , registrationEntity);

        return new EventRegistration(
                registrationEntity.getId(),
                registrationEntity.getId(),
                registrationEntity.getEvent().getId(),
                registrationEntity.getDateRegistration()
        );
    }

   @Override
   public List<EventRegistration> toDomain(@NotNull List<EventRegistrationEntity> registrationEntities) {
       LOGGER.info("Execute method toDomain in RegistrationEntityMapper,registrationEntities = {}"
               , registrationEntities);

        return registrationEntities.stream()
                .map(this::toDomain)
                .toList();
    }
}
