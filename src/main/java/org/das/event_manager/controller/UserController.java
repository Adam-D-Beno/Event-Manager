package org.das.event_manager.controller;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignUpRequest;
import org.das.event_manager.dto.UserDto;
import org.das.event_manager.mappers.UserDtoMapper;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody SignUpRequest signUpRequest) {
        LOGGER.info("Post request for SignUp: login = {}", signUpRequest.login());
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(userDtoMapper.toDto(userService.register(userDtoMapper.toDomain(signUpRequest))));
    }
}
