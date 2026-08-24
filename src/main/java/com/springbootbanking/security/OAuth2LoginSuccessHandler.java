package com.springbootbanking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootbanking.dto.auth.LoginResponse;
import com.springbootbanking.entity.AuthProvider;
import com.springbootbanking.entity.Customer;
import com.springbootbanking.repository.CustomerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public OAuth2LoginSuccessHandler(JwtTokenProvider tokenProvider,
                                     CustomerRepository customerRepository,
                                     PasswordEncoder passwordEncoder,
                                     ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        final AuthProvider provider = "github".equalsIgnoreCase(clientRegistrationId) 
                ? AuthProvider.GITHUB 
                : AuthProvider.GOOGLE;

        final String providerId;
        final String email;
        final String displayName;

        if (provider == AuthProvider.GITHUB) {
            providerId = String.valueOf(attributes.get("id"));
            email = (String) attributes.get("email");
            String rawName = (String) attributes.get("name");
            if (rawName == null || rawName.isBlank()) {
                rawName = (String) attributes.get("login");
            }
            displayName = rawName;
        } else {
            // Google
            providerId = (String) attributes.get("sub");
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
        }

        Customer customer = getOrCreateCustomer(provider, providerId, email, displayName);

        // Generate JWT
        String token = tokenProvider.generateToken(customer.getUsername());
        Integer accountNumber = (customer.getAccount() != null) ? customer.getAccount().getAccountNumber() : null;

        // Redirect browser to dashboard with token parameters
        String redirectUrl = String.format("/index.html?token=%s&username=%s&accountNumber=%s&registrationComplete=%s",
                java.net.URLEncoder.encode(token, java.nio.charset.StandardCharsets.UTF_8),
                java.net.URLEncoder.encode(customer.getUsername(), java.nio.charset.StandardCharsets.UTF_8),
                (accountNumber != null) ? accountNumber.toString() : "",
                customer.isRegistrationComplete());

        response.sendRedirect(redirectUrl);
    }

    private Customer getOrCreateCustomer(AuthProvider provider, String providerId, String email, String displayName) {
        return customerRepository.findByAuthProviderAndOauthProviderId(provider, providerId)
                .or(() -> (email != null && !email.isBlank()) ? customerRepository.findByEmail(email) : java.util.Optional.empty())
                .orElseGet(() -> {
                    String baseUsername = (email != null && !email.isBlank())
                            ? email.split("@")[0].replaceAll("[^a-zA-Z0-9_]", "")
                            : (displayName != null ? displayName.replaceAll("\\s+", "").toLowerCase() : "user");
                    if (baseUsername.length() < 5) {
                        baseUsername = baseUsername + "12345";
                    }
                    String uniqueUsername = baseUsername;
                    int suffix = 1;
                    while (customerRepository.existsByUsername(uniqueUsername)) {
                        uniqueUsername = baseUsername + suffix++;
                    }

                    String randomPassword = passwordEncoder.encode(UUID.randomUUID().toString());
                    Customer newCustomer = new Customer(
                            displayName != null ? displayName : uniqueUsername,
                            uniqueUsername,
                            email,
                            provider,
                            providerId,
                            randomPassword
                    );
                    return customerRepository.save(newCustomer);
                });
    }
}

