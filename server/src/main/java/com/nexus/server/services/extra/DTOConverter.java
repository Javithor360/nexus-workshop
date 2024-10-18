package com.nexus.server.services.extra;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.Project;
import com.nexus.server.entities.User;
import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.entities.dto.ProjectDTO;
import com.nexus.server.entities.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DTOConverter {
    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getDui(),
                user.getEmail(),
                user.getGender(),
                user.getBirthday()
        );
    }

    public ActivityDTO convertToDTO(Activity activity, UserDTO userDTO) {
        return new ActivityDTO(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getType(),
                activity.getCreatedAt(),
                activity.getPercentage(),
                userDTO
        );
    }

    public ProjectDTO convertToDTO(Project project, UserDTO userDTO, List<ActivityDTO> activityDTOs) {
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
    }
}
