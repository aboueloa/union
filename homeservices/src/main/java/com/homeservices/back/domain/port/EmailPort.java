package com.homeservices.back.domain.port;

public interface EmailPort {
    void send(String to, String email);
}
