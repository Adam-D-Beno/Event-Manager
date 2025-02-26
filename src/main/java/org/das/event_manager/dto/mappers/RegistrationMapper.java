package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.EventRegistration;
import org.das.event_manager.domain.entity.EventRegistrationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RegistrationMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationMapper.class);
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    public RegistrationMapper(
            EventMapper eventMapper,
            UserMapper userMapper
    ) {
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }


   public EventRegistration toDomain(EventRegistrationEntity registrationEntity) {
       LOGGER.info("Execute method toDomain in RegistrationEntityMapper,registrationEntity = {}"
               , registrationEntity);

        return new EventRegistration(
                registrationEntity.getId(),
                userMapper.toDomain(registrationEntity.getUserEntity()),
                eventMapper.toDomain(registrationEntity.getEvent()),
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
}
