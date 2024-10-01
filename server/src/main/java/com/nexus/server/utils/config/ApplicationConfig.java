package com.nexus.server.utils.config;

import com.nexus.server.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    @Autowired
    private IUserRepository userRepository;

    // Set the authentication manager bean to be used in the authentication process
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Return default authentication manager
    }

    // Set the authentication provider bean to be used in the authentication process
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); // Create a new DaoAuthenticationProvider object to be used as the authentication provider
        authenticationProvider.setUserDetailsService(userDetailsService()); // Set the user details service to be used in the authentication process
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Set the password encoder to be used in the authentication process
        return authenticationProvider; // Return the authentication provider
    }

    // Set the user details service bean to be used in the authentication process
    @Bean
    protected UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Return the user details service by finding the user by username on the user repository
    }

    // Set the password encoder bean to be used in the authentication process
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Return a new BCryptPasswordEncoder object to be used as the password encoder
    }
}
