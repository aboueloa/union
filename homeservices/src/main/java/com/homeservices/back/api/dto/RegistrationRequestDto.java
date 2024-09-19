package com.homeservices.back.api.dto;

import com.homeservices.back.domain.models.AppUserRole;

public record RegistrationRequestDto(String firstName, String lastName, String email, String pwd, AppUserRole role) {
}
