package org.das.event_manager.controller;

import jakarta.validation.Valid;
import org.das.event_manager.dto.JwtResponse;
import org.das.event_manager.dto.SignInRequest;
import org.das.event_manager.dto.SignUpRequest;
import org.das.event_manager.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {

     ResponseEntity<UserResponseDto> register(SignUpRequest signUpRequest);
     ResponseEntity<UserResponseDto> findById( Long id);
     ResponseEntity<JwtResponse> authentication(SignInRequest signInRequest);
}
