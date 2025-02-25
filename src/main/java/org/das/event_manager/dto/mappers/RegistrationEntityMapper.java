package org.das.event_manager.dto.mappers;

import org.das.event_manager.domain.Registration;
import org.das.event_manager.domain.entity.RegistrationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RegistrationEntityMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationEntityMapper.class);

    //todo завершить маппер
   public Registration toDomain(RegistrationEntity registrationEntity) {
       LOGGER.info("Execute method toDomain in RegistrationEntityMapper,registrationEntity = {}"
               , registrationEntity);
        return null;
    }

   public List<Registration> toDomain(List<RegistrationEntity> registrationEntities) {
       LOGGER.info("Execute method toDomain in RegistrationEntityMapper,registrationEntities = {}"
               , registrationEntities);
        return null;
    }
}
