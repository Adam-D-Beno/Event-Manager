package org.das.event_manager.security;

import org.das.event_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailService.class);
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        LOGGER.info("Execute method loadUserByUsername user: login = {} in CustomUserDetailService class", login);
        return userRepository.findByLogin(login)
                .map(user -> new User(
                        user.getLogin(),
                        user.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with user login: = %s"
                        .formatted(login)));

    }
}
