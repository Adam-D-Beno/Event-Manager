package org.das.event_manager.service;

import org.das.event_manager.domain.User;

public interface UserService {

    public User save(User userToSave);
    public User findByLogin(String login);
    public User findById(Long userId);
    public boolean isUserExistsByLogin(String login);
}
