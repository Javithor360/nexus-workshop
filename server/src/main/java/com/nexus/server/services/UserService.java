package com.nexus.server.services;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.Log;
import com.nexus.server.entities.Project;
import com.nexus.server.entities.User;
import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.entities.dto.ProjectDTO;
import com.nexus.server.entities.dto.UserDTO;
import com.nexus.server.repositories.IActivityRepository;
import com.nexus.server.repositories.ILogRepository;
import com.nexus.server.repositories.IProjectRepository;
import com.nexus.server.repositories.IUserRepository;
import com.nexus.server.services.extra.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IProjectRepository projectRepository;
    private final IActivityRepository activityRepository;
    private final ILogRepository logRepository;
    private final DTOConverter dtoConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository, IProjectRepository projectRepository, IActivityRepository activityRepository, ILogRepository logRepository, DTOConverter dtoConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.activityRepository = activityRepository;
        this.logRepository = logRepository;
        this.dtoConverter = dtoConverter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get all users
     *
     * @return List of all users
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getDui(),
                        user.getEmail(),
                        user.getGender(),
                        user.getBirthday()))
                .collect(Collectors.toList());
    }

    /**
     * Get user by id
     *
     * @param id User id
     * @return User
     */
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getDui(),
                        user.getEmail(),
                        user.getGender(),
                        user.getBirthday()));
    }

    /**
     * Get user by username
     *
     * @param username Username
     * @return User
     */
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getDui(),
                        user.getEmail(),
                        user.getGender(),
                        user.getBirthday()));
    }

    /**
     * Get user by email
     *
     * @param email Email
     * @return User
     */
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getDui(),
                        user.getEmail(),
                        user.getGender(),
                        user.getBirthday()));
    }

    /**
     * Create user
     *
     * @param user User
     * @return User
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Update user
     *
     * @param id          User id
     * @param userDetails User details
     * @return User
     */
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setPassword(userDetails.getPassword());
                    user.setRole(userDetails.getRole());
                    user.setUsername(userDetails.getUsername());
                    user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    user.setDui(userDetails.getDui());
                    user.setEmail(userDetails.getEmail());
                    user.setGender(userDetails.getGender());
                    user.setBirthday(userDetails.getBirthday());
                    return userRepository.save(user);
                });
    }

    /**
     * Delete user
     *
     * @param id User id
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Get the user activities by user id
     *
     * @param id User id
     * @return List Activities
     */
    public Optional<List<ActivityDTO>> getUserActivities(Long id) {
        List<Activity> activities = activityRepository.findByUserId(id);
        List<ActivityDTO> activityDTOs = activities.stream()
                .map(activity -> dtoConverter.convertToDTO(activity, dtoConverter.convertToDTO(activity.getUser())))
                .collect(Collectors.toList());
        return Optional.of(activityDTOs);
    }

    /**
     * Get the projects assigned to a user id
     *
     * @param id User id
     * @return List of projects
     */
    public Optional<List<ProjectDTO>> getUserProjects(Long id) {
        List<Project> projects = projectRepository.findByUserId(id);
        List<ProjectDTO> projectDTOs = projects.stream().map(project -> {
            List<Log> logs = logRepository.findByProjectId(project.getId()).orElseThrow();
            List<ActivityDTO> activityDTOs = logs.stream()
                    .map(Log::getActivity)
                    .map(activity -> dtoConverter.convertToDTO(activity, dtoConverter.convertToDTO(activity.getUser())))
                    .collect(Collectors.toList());

            UserDTO userDTO = this.convertToDTO(project.getUser());

            return new ProjectDTO(
                    project.getId(),
                    project.getClient(),
                    userDTO,
                    project.getTitle(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getDueDate(),
                    activityDTOs
            );
        }).collect(Collectors.toList());

        return Optional.of(projectDTOs);
    }

    // Helper method to convert User to UserDTO
    public UserDTO convertToDTO(User user) {
        return dtoConverter.convertToDTO(user);
    }
}
