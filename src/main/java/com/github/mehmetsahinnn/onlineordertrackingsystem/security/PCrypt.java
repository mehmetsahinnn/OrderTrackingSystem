package com.github.mehmetsahinnn.onlineordertrackingsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Configuration class for password encryption using BCrypt.
 * This class provides a BCryptPasswordEncoder bean for password encryption.
 */
@Configuration
@Component
public class PCrypt {

    /**
     * Configures a BCryptPasswordEncoder bean.
     *
     * @return a BCryptPasswordEncoder bean for password encryption
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
