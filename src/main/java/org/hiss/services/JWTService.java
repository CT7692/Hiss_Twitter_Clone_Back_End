package org.hiss.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken(String name);
    String extractName(String token);
    boolean validateToken(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
}
