package org.das.event_manager.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

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
//                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProviderDao = new DaoAuthenticationProvider();
        authProviderDao.setUserDetailsService(userDetailsService);
        authProviderDao.setPasswordEncoder(passwordEncoder());
        return authProviderDao;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
