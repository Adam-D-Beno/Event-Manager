package org.das.event_manager.service;

import org.das.event_manager.domain.Event;
import org.das.event_manager.domain.Registration;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.EventEntity;
import org.das.event_manager.domain.entity.RegistrationEntity;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.mappers.EventEntityMapper;
import org.das.event_manager.dto.mappers.RegistrationEntityMapper;
import org.das.event_manager.dto.mappers.UserEntityMapper;
import org.das.event_manager.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationOnEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationOnEventService.class);
    private final RegistrationRepository registrationRepository;
    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final RegistrationEntityMapper registrationEntityMapper;

    public RegistrationOnEventService(
            RegistrationRepository registrationRepository,
            EventEntityMapper eventEntityMapper,
            UserEntityMapper userEntityMapper,
            RegistrationEntityMapper registrationEntityMapper
    ) {
        this.registrationRepository = registrationRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.userEntityMapper = userEntityMapper;
        this.registrationEntityMapper = registrationEntityMapper;
    }

    public void registerUserOnEvent(Event event, User currentAuthUser) {
        LOGGER.info("Execute method registerUserOnEvent in RegistrationOnEventService, event = {}, user = {}",
                event, currentAuthUser);
        UserEntity userEntity = userEntityMapper.toEntity(currentAuthUser);
        EventEntity eventEntity = eventEntityMapper.toEntity(event);
        RegistrationEntity newRegistrationOnEvent = new RegistrationEntity(
                null,
                userEntity,
                eventEntity
        );
        registrationRepository.save(newRegistrationOnEvent);
    }

    public List<Registration> findAllByUserRegistration(User currentAuthUser) {
        LOGGER.info("Execute method findAllByUserRegistration in RegistrationOnEventService,user = {}"
                , currentAuthUser);
        return registrationEntityMapper.toDomain(registrationRepository.findAllByUser_Id(currentAuthUser.id()));
    }
}
