package com.nexus.server.utils.config;

import com.nexus.server.utils.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injecting the required dependencies
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    // Initializing dependencies
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authProvider = authProvider;
    }

    // Main filter chain where routes are protected and extra config is defined for the server
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOriginPattern("*"); // Allow all origins (domains) to access the API
                    config.addAllowedMethod("*"); // Allow all methods (GET, POST, PUT, DELETE, etc.)
                    config.addAllowedHeader("*"); // Allow all headers (Authorization, Content-Type, etc.)
                    config.setAllowCredentials(true); // Allow credentials (cookies, authentication, etc.)
                    return config;
                }))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/projects/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement((sessionManager) -> sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Disable default sessions to use JWT
                .authenticationProvider(authProvider) // To be sure define the Authentication Provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT filter
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
