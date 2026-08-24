package com.springbootbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootbanking.dto.auth.OAuthRegistrationCompleteRequest;
import com.springbootbanking.dto.registration.RegistrationRequest;
import com.springbootbanking.dto.registration.RegistrationResponse;
import com.springbootbanking.entity.Gender;
import com.springbootbanking.security.JwtAuthenticationEntryPoint;
import com.springbootbanking.security.JwtAuthenticationFilter;
import com.springbootbanking.security.OAuth2LoginSuccessHandler;
import com.springbootbanking.security.SecurityConfig;
import com.springbootbanking.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = RegistrationController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        SecurityConfig.class,
                        JwtAuthenticationFilter.class,
                        JwtAuthenticationEntryPoint.class,
                        OAuth2LoginSuccessHandler.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should register user successfully and return HTTP 201 Created")
    void testCreateRegister_Success() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "John Doe", LocalDate.of(1990, 1, 1), Gender.MALE, 
                "johndoe123", "password123", "password123", "1234"
        );

        RegistrationResponse response = new RegistrationResponse(true, "Registration successful", 100001);

        when(registrationService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/v3/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.accountNumber").value(100001));
    }

    @Test
    @DisplayName("Should return HTTP 400 Bad Request when validation fails")
    void testCreateRegister_ValidationError() throws Exception {
        // Invalid request: empty fullName and short PIN
        RegistrationRequest invalidRequest = new RegistrationRequest(
                "", LocalDate.of(1990, 1, 1), Gender.MALE, 
                "johndoe123", "password123", "password123", "12"
        );

        mockMvc.perform(post("/api/v3/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should complete OAuth registration successfully and return HTTP 200 OK")
    void testCompleteRegistration_Success() throws Exception {
        OAuthRegistrationCompleteRequest request = new OAuthRegistrationCompleteRequest(
                LocalDate.of(1995, 5, 20), Gender.MALE, "4321"
        );

        RegistrationResponse response = new RegistrationResponse(true, "Registration completed successfully", 100002);

        when(registrationService.completeOAuthRegistration(eq("testuser"), any())).thenReturn(response);

        Principal principal = () -> "testuser";

        mockMvc.perform(post("/api/v3/register/complete")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration completed successfully"))
                .andExpect(jsonPath("$.accountNumber").value(100002));
    }
}
