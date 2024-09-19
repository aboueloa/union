package com.homeservices.back.domain.services;

import com.homeservices.back.domain.exception.UserConfirmationException;
import com.homeservices.back.domain.models.AppUser;
import com.homeservices.back.domain.models.RegistrationRequest;
import com.homeservices.back.domain.port.RegistrationPort;

public class RegistrationService {
    private final RegistrationPort registrationPort;

    public RegistrationService(RegistrationPort registrationPort) {
        this.registrationPort = registrationPort;
    }

    public void register(RegistrationRequest registrationRequest) {
        AppUser appUser = new AppUser();
        appUser.setEmail(registrationRequest.email());
        appUser.setPwd(registrationRequest.pwd());
        appUser.setFirstName(registrationRequest.firstName());
        appUser.setLastName(registrationRequest.lastName());
        appUser.setRole(registrationRequest.role());
        registrationPort.register(appUser);
    }

    public void confirm(String token) throws UserConfirmationException {
        registrationPort.confirm(token);
    }
}
