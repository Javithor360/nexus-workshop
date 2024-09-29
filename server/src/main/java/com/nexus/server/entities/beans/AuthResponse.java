package com.nexus.server.entities.beans;

import com.nexus.server.entities.User;

// Bean class to hold the JWT token response
public class AuthResponse {
    private User user;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
