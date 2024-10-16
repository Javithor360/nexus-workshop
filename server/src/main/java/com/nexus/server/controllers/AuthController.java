package com.nexus.server.controllers;

import com.nexus.server.entities.beans.AuthResponse;
import com.nexus.server.entities.beans.LoginRequest;
import com.nexus.server.services.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // http://localhost:8081/api/auth/
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Main login endpoint
     * @param request LoginRequest object with username and password
     * @return AuthResponse object with JWT token
     * @route POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
