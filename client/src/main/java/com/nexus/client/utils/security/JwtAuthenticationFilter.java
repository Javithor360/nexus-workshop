package com.nexus.client.utils.security;

import com.nexus.client.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// This class is used to authenticate the user based on the session
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Method to filter the incoming requests and authenticate the user through servlet
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Get the session object

        if (session != null) { // If the session is not null
            User user = (User) session.getAttribute("user"); // Get the user object from the session
            String token = (String) session.getAttribute("token"); // Get the token from the session

            if (user != null && token != null) {
                List<GrantedAuthority> authorities = new ArrayList<>(); // Create a list of authorities
                String roleName = user.getRole().getName().toUpperCase(); // Get the role name
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName)); // Add the role to the authorities

                switch (roleName) {
                    case "ADMIN":
                        authorities.add(new SimpleGrantedAuthority("ACCESS_ADMIN_DASHBOARD")); // Add the authority based on the role
                        break;
                    case "BOSS":
                        authorities.add(new SimpleGrantedAuthority("ACCESS_BOSS_DASHBOARD")); // Add the authority based on the role
                        break;
                    case "EMPLOYEE":
                        authorities.add(new SimpleGrantedAuthority("ACCESS_EMPLOYEE_DASHBOARD")); // Add the authority based on the role
                        break;
                }

                // Create an authentication token with the user, token, and authorities
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}