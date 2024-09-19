package com.homeservices.back.domain.port;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtPort {
    String generateToken(String userName);

    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
