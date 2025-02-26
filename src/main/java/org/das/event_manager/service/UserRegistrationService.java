package org.das.event_manager.service;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.User;

public interface UserRegistrationService {

    User register(@NotNull User signUpRequest);
}
