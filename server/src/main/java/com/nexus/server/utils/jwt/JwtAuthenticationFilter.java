package com.nexus.server.utils.jwt;

import com.nexus.server.services.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        try {
            // If the token is null, proceed to the next filter
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract the username from the token
            username = jwtService.getUsernameFromToken(token);

            // If the username is not null and the authentication context is null, load the user
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // If the token is valid, set the authentication token
                if(jwtService.isValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set the authentication context with the token
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            // Token has expired
            handleExceptionResponse(response, "Token expired", HttpStatus.UNAUTHORIZED);
            return;
        } catch (UnsupportedJwtException e) {
            // Token type is not supported
            handleExceptionResponse(response, "Unsupported JWT token", HttpStatus.UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            // Token is malformed
            handleExceptionResponse(response, "Malformed JWT token", HttpStatus.UNAUTHORIZED);
            return;
        } catch (SignatureException e) {
            // Signature validation failed
            handleExceptionResponse(response, "Invalid JWT signature", HttpStatus.UNAUTHORIZED);
            return;
        } catch (IllegalArgumentException e) {
            // Invalid token
            handleExceptionResponse(response, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            return;
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

    // Helper method to send error response when token is invalid
    private void handleExceptionResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
