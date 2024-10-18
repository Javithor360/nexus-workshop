package com.nexus.server.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nexus.server.entities.Role;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String username;
    private Role role;
    private String dui;
    private String email;
    private String gender;
    private LocalDate birthday;

    public UserDTO(Long id, String username, Role role, String dui, String email, String gender, LocalDate birthday) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.dui = dui;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
