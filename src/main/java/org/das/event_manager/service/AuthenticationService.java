package org.das.event_manager.service;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignInRequest;

public interface AuthenticationService {

    String authenticateUser(SignInRequest signInRequest);

    User getCurrentAuthenticatedUser();
}
