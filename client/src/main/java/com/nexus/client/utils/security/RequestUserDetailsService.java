package com.nexus.client.utils.security;

import com.nexus.client.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RequestUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;
    private final String SERVER_URL = "http://localhost:8081/api";

    public RequestUserDetailsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = restTemplate.getForObject(SERVER_URL + "/users/username/" + username, User.class);

        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().toUpperCase());

        return new ExtendedUserDetails(user, Collections.singletonList(authority));
    }
}
