package com.homeservices.back.domain.models;

import lombok.Builder;

@Builder
public record RegistrationRequest(String firstName, String lastName, String email, String pwd, AppUserRole role) {
}
