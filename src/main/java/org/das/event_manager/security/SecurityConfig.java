package org.das.event_manager.security;


import org.das.event_manager.domain.UserRole;
import org.das.event_manager.exeption.CustomAccessDeniedHandler;
import org.das.event_manager.exeption.CustomAuthenticationEntryPoint;
import org.das.event_manager.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            JwtTokenFilter jwtTokenFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true).ignoring()
                .requestMatchers("/css/**",
                        "/js/**",
                        "/img/**",
                        "/lib/**",
                        "/favicon.ico",
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v3/api-docs/swagger-config",
                        "/openapi.yaml"
                );
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
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .requestMatchers(HttpMethod.POST, "/locations")
                            .hasAuthority(UserRole.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE, "/locations/**")
                            .hasAuthority(UserRole.ADMIN.name())
                            .requestMatchers(HttpMethod.PUT, "/locations/**")
                            .hasAuthority(UserRole.ADMIN.name())

                            .requestMatchers(HttpMethod.POST, "/users")
                            .permitAll()
                            .requestMatchers(HttpMethod.GET, "/users/**")
                            .hasAuthority(UserRole.ADMIN.name())
                            .requestMatchers(HttpMethod.POST, "/users/auth")
                            .permitAll()

                            .requestMatchers(HttpMethod.POST, "/events")
                            .hasAnyAuthority(UserRole.USER.name())
                            .requestMatchers(HttpMethod.DELETE, "/events/**")
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .requestMatchers(HttpMethod.GET, "/events/**")
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .requestMatchers(HttpMethod.PUT, "/events/**")
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .requestMatchers(HttpMethod.POST, "/events/search")
                            .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                            .requestMatchers(HttpMethod.GET, "/events/my")
                            .hasAnyAuthority(UserRole.USER.name())
                            .requestMatchers(HttpMethod.POST, "/events/registrations/**")
                            .hasAnyAuthority(UserRole.USER.name())
                            .requestMatchers(HttpMethod.DELETE, "/events/registrations/cancel/**")
                            .hasAnyAuthority(UserRole.USER.name())
                            .requestMatchers(HttpMethod.GET, "/events/registrations/my")
                            .hasAnyAuthority(UserRole.USER.name())

                            .anyRequest()
                            .authenticated();
                })
                .exceptionHandling(exception -> {
                                exception.authenticationEntryPoint(authenticationEntryPoint);
                                exception.accessDeniedHandler(customAccessDeniedHandler);
                })
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
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
