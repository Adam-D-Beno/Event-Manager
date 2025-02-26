package org.das.event_manager.service;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignInRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface AuthenticationService {

    String authenticateUser(@NotNull SignInRequest signInRequest);

    User getCurrentAuthenticatedUserOrThrow();
}
