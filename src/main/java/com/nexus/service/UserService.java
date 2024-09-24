package com.nexus.service;

import com.nexus.entities.User;
import com.nexus.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by id
     * @param id User id
     * @return User
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Create user
     * @param user User
     * @return User
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Update user
     * @param id User id
     * @param userDetails User details
     * @return User
     */
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setPassword(userDetails.getPassword());
                    user.setRole(userDetails.getRole());
                    user.setDui(userDetails.getDui());
                    user.setEmail(userDetails.getEmail());
                    user.setGender(userDetails.getGender());
                    user.setBirthday(userDetails.getBirthday());
                    return userRepository.save(user);
                });
    }

    /**
     * Delete user
     * @param id User id
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Get user by username
     * @param username User username
     * @return User
     */
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
