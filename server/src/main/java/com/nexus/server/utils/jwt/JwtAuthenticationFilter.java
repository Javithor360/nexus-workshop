package com.nexus.server.utils.jwt;

import com.nexus.server.services.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Injecting the required dependencies
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Constructor to initialize the required dependencies
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Method to filter the incoming requests and authenticate the user
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenRequest(request); // Get the token from the request
        final String username; // Variable to store the username

        if (token == null) {
            filterChain.doFilter(request, response); // If token is null, pass the request
            return;
        }

        // If the token is not null, get the username from the token
        username = jwtService.getUsernameFromToken(token);

        // If the username is not null and the authentication context is null, load the user by username
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Load the user by username

            // If the token is valid, set the authentication token
            if(jwtService.isValid(token, userDetails)) {
                // Create the authentication token and set the authentication context with the token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication context with the token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request
        filterChain.doFilter(request, response);
    }

    private String getTokenRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
