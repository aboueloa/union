package com.homeservices.back.domain.port;

import com.homeservices.back.domain.exception.UserConfirmationException;
import com.homeservices.back.domain.models.AppUser;

public interface RegistrationPort {
    void register(AppUser appUser);

    void confirm(String token) throws UserConfirmationException;
}
