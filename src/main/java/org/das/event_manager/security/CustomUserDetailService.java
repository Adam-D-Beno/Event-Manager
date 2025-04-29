package org.das.event_manager.security;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailService.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        LOGGER.info("Execute method loadUserByUsername user: login = {} in CustomUserDetailService class", login);

        return userRepository.findByLogin(login)
                .map(userEntity -> new User(
                        userEntity.getLogin(),
                        userEntity.getPasswordHash(),
                        List.of(new SimpleGrantedAuthority(userEntity.getUserRole().name()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with user login: = %s"
                        .formatted(login)));

    }
}
