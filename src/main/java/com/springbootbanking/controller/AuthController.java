package com.springbootbanking.controller;

import com.springbootbanking.dto.auth.LoginRequest;
import com.springbootbanking.dto.auth.LoginResponse;
import com.springbootbanking.entity.Customer;
import com.springbootbanking.repository.CustomerRepository;
import com.springbootbanking.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomerRepository customerRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Customer customer = customerRepository.findByUsername(request.getUsername())
                .or(() -> customerRepository.findByEmail(request.getUsername()))
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.resetFailedLogins();
        customerRepository.save(customer);

        String token = tokenProvider.generateToken(customer.getUsername());
        Integer accountNumber = (customer.getAccount() != null) ? customer.getAccount().getAccountNumber() : null;

        LoginResponse response = new LoginResponse(
                token,
                "Bearer",
                customer.getUsername(),
                accountNumber,
                customer.isRegistrationComplete()
        );

        return ResponseEntity.ok(response);
    }
}

