package com.homeservices.back.api.controller;

import com.homeservices.back.api.dto.LoginRequestDto;
import com.homeservices.back.api.dto.RegistrationRequestDto;
import com.homeservices.back.domain.exception.UserConfirmationException;
import com.homeservices.back.domain.models.RegistrationRequest;
import com.homeservices.back.domain.services.JwtService;
import com.homeservices.back.domain.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        var registrationRequest = RegistrationRequest.builder()
                .firstName(registrationRequestDto.firstName())
                .lastName(registrationRequestDto.lastName())
                .email(registrationRequestDto.email())
                .pwd(registrationRequestDto.pwd())
                .role(registrationRequestDto.role())
                .build();
        registrationService.register(registrationRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<Void> confirm(@RequestParam("token") String token) throws UserConfirmationException {
        registrationService.confirm(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.pwd()));
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(loginRequestDto.username()), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
    }
}
