package com.nexus.client.services;

import com.nexus.client.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {
    @Value("${api.baseURL}")
    private String BASE_URL;

    private final RestTemplate restTemplate;

    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Fetch the specific project and include JWT from the session
    public Project getProject(int id, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Set JWT in the header

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Project> response = restTemplate.exchange(
                    BASE_URL + "/projects/" + id, HttpMethod.GET, entity, Project.class
            );

            // Returns the body of the response if success
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;

        } catch (HttpClientErrorException.NotFound ex) {
            return null; // If receive an error 404 it returns null to indicate that the project doesn't exist
        }
    }
}
