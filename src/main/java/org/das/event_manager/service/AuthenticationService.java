package org.das.event_manager.service;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignInRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface AuthenticationService {

    String authenticateUser(SignInRequest signInRequest);

    User getCurrentAuthenticatedUserOrThrow();
}
