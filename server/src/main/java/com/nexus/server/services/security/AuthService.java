package com.nexus.server.services.security;

import com.nexus.server.entities.User;
import com.nexus.server.entities.beans.AuthResponse;
import com.nexus.server.entities.beans.LoginRequest;
import com.nexus.server.repositories.IUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Injecting the required dependencies
    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Constructor to initialize the required dependencies
    public AuthService(IUserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Service method to authenticate the user and generate JWT token
     * @param request LoginRequest object with username and password
     * @return AuthResponse object with JWT token
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
