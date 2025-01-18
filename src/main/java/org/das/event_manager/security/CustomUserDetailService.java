package org.das.event_manager.security;

import org.das.event_manager.entity.UserEntity;
import org.das.event_manager.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        UserEntity found = userRepository.findByLogin(login);
//        return User.withUsername(found.getLogin())
//                .password(found.getPassword())
//                .authorities(found.getRole())
//                .build();
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
