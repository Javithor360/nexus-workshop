package com.nexus.server.services.security;

import com.nexus.server.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Injecting the secret key from the application.properties file
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long EXPIRATION_TIME;

    /**
     * Main method to generate a JWT token for a given user
     *
     * @param user
     * @return JWT token
     */
    public String generateToken(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        /* IN CASE OF NEEDING TO ADD EXTRA CLAIMS */
        // claims.put("role", user.getRole().getName());
        // claims.put("email", user.getEmail());
        // etc...
        return generateToken(claims, user.getUsername());
    }

    /**
     * Helper method to build a complete JWT token with extra claims and expiration time of 24 hours
     * @param extraClaims Extra claims list to add to the token
     * @param username User's username
     * @return Signed JWT token
     */
    private String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 2 hours
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract the username from a given JWT token
     * @param token JWT token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Validate if a given token is valid for a given user
     * @param token JWT token
     * @param userDetails User details
     * @return True if the token is valid, false otherwise
     */
    public boolean isValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    /**
     * Generic method to extract a claim from a given token
     * @param token JWT token
     * @param claimsResolver Function to extract the claim
     * @return Claim value
     * @param <T> Type of the claim
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    // Helper methods

    private Date getExpirationDateFromToken(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }
}
