package org.das.event_manager.controller;

import org.das.event_manager.domain.User;
import org.das.event_manager.dto.UserDto;
import org.das.event_manager.mappers.UserDtoMapper;
import org.das.event_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(userDtoMapper.toDto(userService.register(userDtoMapper.toDomain(userDto))));
    }
}
