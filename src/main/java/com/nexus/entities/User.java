package com.nexus.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "The 'username' field is mandatory")
    @Column(name = "username", nullable = false, length = 32)
    private String username;

    @NotNull(message = "The 'password' field is mandatory")
    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @NotNull(message = "The 'role' field is mandatory")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @NotEmpty(message = "The 'dui' field is mandatory")
    @Column(name = "dui", nullable = false, length = 10)
    private String dui;

    @NotEmpty(message = "The 'email' field is mandatory")
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @NotEmpty(message = "The 'gender' field is mandatory")
    @Lob
    @Column(name = "gender", nullable = false)
    private String gender;

    @NotNull(message = "The 'birthday' field is mandatory")
    @Column(name = "birthday")
    private LocalDate birthday;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

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