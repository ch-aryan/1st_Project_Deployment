package com.springbootbanking.controller;

/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootbanking.dto.auth.LoginRequest;
import com.springbootbanking.entity.Customer;
import com.springbootbanking.entity.Gender;
import com.springbootbanking.repository.CustomerRepository;
import com.springbootbanking.security.JwtAuthenticationEntryPoint;
import com.springbootbanking.security.JwtAuthenticationFilter;
import com.springbootbanking.security.JwtTokenProvider;
import com.springbootbanking.security.OAuth2LoginSuccessHandler;
import com.springbootbanking.security.SecurityConfig;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
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
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtTokenProvider tokenProvider;

    @MockitoBean
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should login successfully and return JWT token")
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("johndoe123", "password123");

        Customer customer = new Customer("John Doe", "johndoe123", "hashedPass",
                LocalDate.of(1990, 1, 1), Gender.MALE, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("johndoe123", "password123"));
        when(customerRepository.findByUsername("johndoe123")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(tokenProvider.generateToken("johndoe123")).thenReturn("mock.jwt.token");

        mockMvc.perform(post("/api/v3/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.username").value("johndoe123"));
    }

    @Test
    @DisplayName("Should return 401 when invalid credentials provided")
    void testLogin_BadCredentials() throws Exception {
        LoginRequest request = new LoginRequest("johndoe123", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/v3/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
*/
