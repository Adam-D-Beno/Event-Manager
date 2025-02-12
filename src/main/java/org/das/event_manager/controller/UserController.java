package org.das.event_manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.dto.JwtResponse;
import org.das.event_manager.dto.SignInRequest;
import org.das.event_manager.dto.SignUpRequest;
import org.das.event_manager.dto.UserResponseDto;
import org.das.event_manager.dto.mappers.UserDtoMapper;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.UserRegistrationService;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final AuthenticationService authenticationService;
    private final UserDtoMapper userDtoMapper;
    private final UserRegistrationService userRegistrationService;
    private final UserService userService;

    public UserController(
            AuthenticationService authenticationService,
            UserDtoMapper userDtoMapper,
            UserRegistrationService userRegistrationService,
            UserService userService
    ) {
        this.authenticationService = authenticationService;
        this.userDtoMapper = userDtoMapper;
        this.userRegistrationService = userRegistrationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        LOGGER.info("Post request for SignUp: login = {}", signUpRequest.login());
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(userDtoMapper.toDto(userRegistrationService.register(userDtoMapper.toDomain(signUpRequest))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@NotNull @PathVariable(name = "id") Long id) {
        return ResponseEntity.
                status(HttpStatus.FOUND)
                .body(userDtoMapper.toDto(userService.findById(id)));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authentication(@Valid @RequestBody SignInRequest signInRequest) {
        LOGGER.info("Post request for SignIn: login = {}", signInRequest.login());
        return ResponseEntity.ok().body(new JwtResponse(authenticationService.authenticateUser(signInRequest)));
    }
}
