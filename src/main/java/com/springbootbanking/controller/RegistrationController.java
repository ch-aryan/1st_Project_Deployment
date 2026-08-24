package com.springbootbanking.controller;

import com.springbootbanking.dto.auth.OAuthRegistrationCompleteRequest;
import com.springbootbanking.dto.registration.RegistrationRequest;
import com.springbootbanking.dto.registration.RegistrationResponse;
import com.springbootbanking.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v3/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> createRegister(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/complete")
    public ResponseEntity<RegistrationResponse> completeRegistration(
            Principal principal,
            @Valid @RequestBody OAuthRegistrationCompleteRequest request) {
        RegistrationResponse response = registrationService.completeOAuthRegistration(principal.getName(), request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}