package com.arsenfesiuk.habit_tracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 1000 * 60 * 60 * 96;

    public String generateToken(Long id, String email, Boolean isProfileCompleted, String role) {
        Map<String, Object> claims = new HashMap<>();

        if (isProfileCompleted != null) {
            claims.put("id", id);
            claims.put("isProfileCompleted", isProfileCompleted);
        }

        if (role != null) {
            claims.put("role", role);
        }

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .addClaims(claims)
                .signWith(key)
                .compact();
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .addClaims(Map.of("role", role))
                .signWith(key)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}


