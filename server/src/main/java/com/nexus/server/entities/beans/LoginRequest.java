package com.nexus.server.entities.beans;

import jakarta.validation.constraints.NotNull;

// Bean class to hold the login request
public class LoginRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Password cannot be null")
    private String password;

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
