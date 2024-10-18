package com.nexus.server.entities.beans;

import com.nexus.server.entities.dto.UserDTO;

// Bean class to hold the JWT token response
public class AuthResponse {
    private UserDTO user;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
