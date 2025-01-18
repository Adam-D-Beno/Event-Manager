package org.das.event_manager.security;

import org.das.event_manager.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(login -> login.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, "/locations/**")
                            .hasAnyAuthority("ADMIN", "USER")
                            .requestMatchers(HttpMethod.POST, "/locations")
                            .hasAnyAuthority("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/locations/**")
                            .hasAnyAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/locations/**")
                            .hasAnyAuthority("ADMIN")

                            .requestMatchers(HttpMethod.POST, "/users")
                            .permitAll()
                            .requestMatchers(HttpMethod.GET, "/users/**")
                            .hasAnyAuthority("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/users/auth")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }


}
