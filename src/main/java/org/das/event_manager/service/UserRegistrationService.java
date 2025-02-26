package org.das.event_manager.service;

import org.das.event_manager.domain.User;

public interface UserRegistrationService {

    public User register(User signUpRequest);
}
