package org.das.event_manager.dto.mappers;

import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.User;
import org.das.event_manager.domain.entity.UserEntity;
import org.das.event_manager.dto.SignUpRequest;
import org.das.event_manager.dto.UserResponseDto;

public interface UserMapper {
    UserResponseDto toDto(User user);
    User toDomain(SignUpRequest signUpRequest);
    UserEntity toEntity(User user);
    User toDomain(UserEntity userEntity);
}
