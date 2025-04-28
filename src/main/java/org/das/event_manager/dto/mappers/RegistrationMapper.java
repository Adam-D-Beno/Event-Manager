package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.EventRegistration;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.das.event_manager.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RegistrationMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationMapper.class);
    private final RegistrationRepository registrationRepository;

    public RegistrationMapper(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

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

   public List<EventRegistration> toDomain(List<EventRegistrationEntity> registrationEntities) {
       LOGGER.info("Execute method toDomain in RegistrationEntityMapper,registrationEntities = {}"
               , registrationEntities);

        return registrationEntities.stream()
                .map(this::toDomain)
                .toList();
    }

    public List<EventRegistrationEntity> toEntity(List<EventRegistration> eventRegistrations) {
        List<Long> registrationIds = eventRegistrations
                .stream()
                .map(EventRegistration::id)
                .toList();
        return registrationRepository.findAllById(registrationIds);
    }
}
