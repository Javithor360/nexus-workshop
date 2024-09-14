package com.nexus.controllers;

import com.nexus.entities.User;
import com.nexus.utils.ResourceNotFoundException;
import com.nexus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users") // http://localhost:8080/api/users
public class UserController {

    /*
        BEFORE CONSUMING THE API MAKE SURE:
        1. You have created a database named "nexus_workshop" in your MySQL provider.
        2. You hava ran JUnit tests to make sure the API is working as expected
           and created default information in the database.
     */
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users
     *
     * @return List of all users
     * @route GET /api/users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Get user by id
     *
     * @param id User id
     * @return User
     * @route GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }

    /**
     * Create user
     *
     * @param user User
     * @return User
     * @route POST /api/users
     */

    /*
        Example of a request body:
        {
            "role": {
                "id": 3
            },
            "dui": "87654321-0",
            "email": "beto@example.com",
            "gender": "M",
            "birthday": "1990-02-01"
        }
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    /**
     * Update user
     *
     * @param id          User id
     * @param userDetails User details
     * @return User
     * @route PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }

    /**
     * Delete user
     *
     * @param id User id
     * @route DELETE /api/users/{id}
     * @return Response message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }
}
