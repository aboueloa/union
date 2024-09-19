package com.homeservices.back.domain.services;

import com.homeservices.back.domain.port.JwtPort;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtService {
    private final JwtPort jwtPort;

    public JwtService(JwtPort jwtPort) {
        this.jwtPort = jwtPort;
    }

    public String generateToken(String userName) {
        return jwtPort.generateToken(userName);
    }

    public String extractUserName(String token) {
        return jwtPort.extractUsername(token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtPort.validateToken(token, userDetails);
    }
}
