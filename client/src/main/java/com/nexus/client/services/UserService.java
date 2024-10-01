package com.nexus.client.services;

import com.nexus.client.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Value("${api.baseURL}")
    private String BASE_URL;

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Fetch users and include JWT from the session
    public List<User> getUsers(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Set the JWT token in the header

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(
                BASE_URL + "/users", HttpMethod.GET, entity, User[].class
        );

        return Arrays.asList(response.getBody());
    }
}
