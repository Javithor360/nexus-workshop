package com.nexus.client.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.client.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.baseURL}")
    private String BASE_URL;

    // Simple GET request to show the login form
    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Check if the user has an active session
            switch (user.getRole().getName().toUpperCase()) {
                case "ADMIN":
                    return "redirect:/dashboard/admin/index";
                case "BOSS":
                    return "redirect:/dashboard/boss/index";
                case "EMPLOYEE":
                    return "redirect:/dashboard/employee/index";
                default:
                    return "redirect:/dashboard";
            }
        }
        // If there's no active session, show the login form
        return "login";
    }

    /**
     * POST request to authenticate the user
     * @param username The username of the user
     * @param password The password of the user
     * @param session The session object
     * @param model The model object
     * @return The redirect URL based on the user's role
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        String loginUrl = BASE_URL + "/auth/login"; // The URL of the login endpoint

        // Set the Content-Type header to application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body with the username and password
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, request, Map.class); // Send the POST request
            Map<String, Object> responseBody = response.getBody(); // Get the response body

            if (responseBody != null && responseBody.containsKey("token")) { // Check if the response body contains a token
                String token = (String) responseBody.get("token"); // Get the token from the response body
                Map<String, Object> userData = (Map<String, Object>) responseBody.get("user"); // Get the user data from the response body

                User user = objectMapper.convertValue(userData, User.class); // Convert the user data to a User object

                // Store the token and user object in the session
                session.setAttribute("token", token);
                session.setAttribute("user", user);

                // Create an authentication token with the user object and the role
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.singletonList(() -> "ROLE_" + user.getRole().getName())
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Redirect the user based on their role
                switch (user.getRole().getName()) {
                    case "ADMIN":
                        return "redirect:/dashboard/admin/index";
                    case "BOSS":
                        return "redirect:/dashboard/boss/index";
                    case "EMPLOYEE":
                        return "redirect:/dashboard/employee/index";
                    default:
                        return "redirect:/dashboard";
                }
            } else {
                // If the response body doesn't contain a token, show an error message
                model.addAttribute("error", "Invalid credentials");
                return "login";
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                model.addAttribute("error", "Invalid username or password. Please try again.");
            } else {
                model.addAttribute("error", "An error occurred. Please try again later.");
            }
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "login";
        }
    }
}