package com.nexus.client.utils.config;

import com.nexus.client.utils.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// This class is used to configure the security settings for the application
@Configuration
@EnableWebSecurity // This annotation enables Spring Security features
public class SecurityConfig {

    // Inject the JWT authentication filter
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Initialize the JWT authentication filter
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // This method is used to configure the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT authentication filter
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/home", "/error/**", "/login", "/css/**", "/js/**", "/img/**", "main.css").permitAll() // Allow access to these paths without authentication
                        .requestMatchers("/dashboard/admin/**").hasAuthority("ACCESS_ADMIN_DASHBOARD") // Allow access to the admin dashboard only to users with the ACCESS_ADMIN_DASHBOARD authority
                        .requestMatchers("/dashboard/boss/**").hasAuthority("ACCESS_BOSS_DASHBOARD") // Allow access to the boss dashboard only to users with the ACCESS_BOSS_DASHBOARD authority
                        .requestMatchers("/dashboard/employee/**").hasAuthority("ACCESS_EMPLOYEE_DASHBOARD") // Allow access to the employee dashboard only to users with the ACCESS_EMPLOYEE_DASHBOARD authority
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .formLogin(AbstractHttpConfigurer::disable) // Disable form login
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/login?logout") // Redirect to the login page after logout
            );

        return http.build();
    }
}