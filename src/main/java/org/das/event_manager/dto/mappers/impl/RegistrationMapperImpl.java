package org.das.event_manager.dto.mappers.impl;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.EventRegistration;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.dto.mappers.RegistrationMapper;
import org.das.event_manager.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
public class RegistrationMapperImpl implements RegistrationMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationMapperImpl.class);
    private final RegistrationRepository registrationRepository;

    public RegistrationMapperImpl(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Override
   public EventRegistration toDomain(EventRegistrationEntity registrationEntity) {
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
   public List<EventRegistration> toDomain(List<EventRegistrationEntity> registrationEntities) {
       LOGGER.info("Execute method toDomain in RegistrationEntityMapper,registrationEntities = {}"
               , registrationEntities);

        return registrationEntities.stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<EventRegistrationEntity> toEntity(List<EventRegistration> eventRegistrations) {
        List<Long> registrationIds = eventRegistrations
                .stream()
                .map(EventRegistration::id)
                .toList();
        return registrationRepository.findAllById(registrationIds);
    }
}
