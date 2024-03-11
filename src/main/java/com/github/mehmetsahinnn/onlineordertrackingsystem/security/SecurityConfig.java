package com.github.mehmetsahinnn.onlineordertrackingsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Configuration class for Spring Security.
 * This class configures security settings for the application, allowing access to certain endpoints without authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures security filters for the HTTP security.
     *
     * @param http the HTTP security object to configure
     * @return the configured SecurityFilterChain object
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs"
                                ).permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
