package org.das.event_manager.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.User;

public interface UserService {

     User save(@NotNull User userToSave);
     User findByLogin(@NotBlank String login);
     User findById(@NotNull Long userId);
     boolean isUserExistsByLogin(@NotBlank String login);
}
