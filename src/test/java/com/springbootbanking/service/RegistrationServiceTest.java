package com.springbootbanking.service;

import com.springbootbanking.dto.auth.OAuthRegistrationCompleteRequest;
import com.springbootbanking.dto.registration.RegistrationRequest;
import com.springbootbanking.dto.registration.RegistrationResponse;
import com.springbootbanking.entity.AuthProvider;
import com.springbootbanking.entity.Customer;
import com.springbootbanking.entity.Gender;
import com.springbootbanking.exception.AccountNotFoundException;
import com.springbootbanking.exception.MinimumAgeRequirementException;
import com.springbootbanking.exception.PasswordMisMatchException;
import com.springbootbanking.exception.UsernameAlreadyExistsException;
import com.springbootbanking.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Query nativeQuery;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.createNativeQuery(anyString())).thenReturn(nativeQuery);
    }

    @Nested
    @DisplayName("Normal Registration Tests (register)")
    class NormalRegistrationTests {

        private RegistrationRequest validRequest;

        @BeforeEach
        void init() {
            validRequest = new RegistrationRequest(
                    "John Doe",
                    LocalDate.of(1995, 6, 15),
                    Gender.MALE,
                    "johndoe",
                    "Password123!",
                    "Password123!",
                    "1234"
            );
        }

        @Test
        @DisplayName("Should successfully register user, hash password, assign account number, and return 201 response")
        void testRegister_Success() {
            when(customerRepository.existsByUsername("johndoe")).thenReturn(false);
            when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hashedPasswordSample");
            when(nativeQuery.getSingleResult()).thenReturn(100001L);

            RegistrationResponse response = registrationService.register(validRequest);

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals("Registration successful", response.getMessage());
            assertEquals(100001, response.getAccountNumber());

            ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
            verify(customerRepository).save(customerCaptor.capture());
            Customer savedCustomer = customerCaptor.getValue();

            assertEquals("John Doe", savedCustomer.getFullName());
            assertEquals("johndoe", savedCustomer.getUsername());
            assertEquals("$2a$10$hashedPasswordSample", savedCustomer.getPassword());
            assertEquals(AuthProvider.LOCAL, savedCustomer.getAuthProvider());
            assertTrue(savedCustomer.isRegistrationComplete());
            assertNotNull(savedCustomer.getAccount());
            assertEquals(100001, savedCustomer.getAccount().getAccountNumber());
            assertEquals("1234", savedCustomer.getAccount().getPin());
        }

        @Test
        @DisplayName("Should throw UsernameAlreadyExistsException when username is already taken")
        void testRegister_UsernameAlreadyExists() {
            when(customerRepository.existsByUsername("johndoe")).thenReturn(true);

            UsernameAlreadyExistsException exception = assertThrows(
                    UsernameAlreadyExistsException.class,
                    () -> registrationService.register(validRequest)
            );

            assertEquals("Username johndoe already exists", exception.getMessage());
            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw PasswordMisMatchException when password and confirmPassword do not match")
        void testRegister_PasswordMismatch() {
            RegistrationRequest mismatchRequest = new RegistrationRequest(
                    "John Doe",
                    LocalDate.of(1995, 6, 15),
                    Gender.MALE,
                    "johndoe",
                    "Password123!",
                    "DifferentPassword456!",
                    "1234"
            );

            when(customerRepository.existsByUsername("johndoe")).thenReturn(false);

            PasswordMisMatchException exception = assertThrows(
                    PasswordMisMatchException.class,
                    () -> registrationService.register(mismatchRequest)
            );

            assertEquals("Password & confirm password do not match", exception.getMessage());
            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw MinimumAgeRequirementException when user is under 18 years old")
        void testRegister_UnderAge() {
            RegistrationRequest underAgeRequest = new RegistrationRequest(
                    "Minor User",
                    LocalDate.now().minusYears(16), // 16 years old
                    Gender.MALE,
                    "minoruser",
                    "Password123!",
                    "Password123!",
                    "1234"
            );

            when(customerRepository.existsByUsername("minoruser")).thenReturn(false);

            MinimumAgeRequirementException exception = assertThrows(
                    MinimumAgeRequirementException.class,
                    () -> registrationService.register(underAgeRequest)
            );

            assertEquals("Age must be 18 or above", exception.getMessage());
            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("OAuth Registration Completion Tests (completeOAuthRegistration)")
    class OAuthRegistrationCompletionTests {

        @Test
        @DisplayName("Should complete OAuth registration, assign bank account and set registrationComplete=true")
        void testCompleteOAuthRegistration_Success() {
            String username = "oauth_google_user";
            OAuthRegistrationCompleteRequest request = new OAuthRegistrationCompleteRequest(
                    LocalDate.of(1998, 3, 20),
                    Gender.FEMALE,
                    "5678"
            );

            Customer incompleteCustomer = new Customer(
                    "OAuth User",
                    username,
                    "user@gmail.com",
                    AuthProvider.GOOGLE,
                    "google-sub-12345",
                    "$2a$10$randomEncodedPassword"
            );

            when(customerRepository.findByUsername(username)).thenReturn(Optional.of(incompleteCustomer));
            when(nativeQuery.getSingleResult()).thenReturn(100002L);

            RegistrationResponse response = registrationService.completeOAuthRegistration(username, request);

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals("Registration completed successfully", response.getMessage());
            assertEquals(100002, response.getAccountNumber());

            assertTrue(incompleteCustomer.isRegistrationComplete());
            assertEquals(LocalDate.of(1998, 3, 20), incompleteCustomer.getDateOfBirth());
            assertEquals(Gender.FEMALE, incompleteCustomer.getGender());
            assertNotNull(incompleteCustomer.getAccount());
            assertEquals(100002, incompleteCustomer.getAccount().getAccountNumber());
            assertEquals("5678", incompleteCustomer.getAccount().getPin());

            verify(customerRepository).save(incompleteCustomer);
        }

        @Test
        @DisplayName("Should throw AccountNotFoundException when OAuth username does not exist")
        void testCompleteOAuthRegistration_CustomerNotFound() {
            String username = "nonexistent_user";
            OAuthRegistrationCompleteRequest request = new OAuthRegistrationCompleteRequest(
                    LocalDate.of(1998, 3, 20),
                    Gender.FEMALE,
                    "5678"
            );

            when(customerRepository.findByUsername(username)).thenReturn(Optional.empty());

            assertThrows(
                    AccountNotFoundException.class,
                    () -> registrationService.completeOAuthRegistration(username, request)
            );

            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw MinimumAgeRequirementException when OAuth user is under 18")
        void testCompleteOAuthRegistration_UnderAge() {
            String username = "young_oauth_user";
            OAuthRegistrationCompleteRequest request = new OAuthRegistrationCompleteRequest(
                    LocalDate.now().minusYears(15), // 15 years old
                    Gender.FEMALE,
                    "5678"
            );

            Customer customer = new Customer(
                    "Young User",
                    username,
                    "young@gmail.com",
                    AuthProvider.GOOGLE,
                    "google-id-1",
                    "hash"
            );

            when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));

            MinimumAgeRequirementException exception = assertThrows(
                    MinimumAgeRequirementException.class,
                    () -> registrationService.completeOAuthRegistration(username, request)
            );

            assertEquals("Age must be 18 or above", exception.getMessage());
            verify(customerRepository, never()).save(any());
        }
    }
}

