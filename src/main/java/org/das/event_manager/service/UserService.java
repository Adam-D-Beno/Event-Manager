package org.das.event_manager.service;

import org.das.event_manager.domain.User;

public interface UserService {

     User save(User userToSave);
     User findByLogin(String login);
     User findById(Long userId);
     boolean isUserExistsByLogin(String login);
}
