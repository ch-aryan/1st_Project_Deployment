// src/main/java/com/springbootbanking/service/RegistrationService.java
package com.springbootbanking.service;

import com.springbootbanking.dto.auth.OAuthRegistrationCompleteRequest;
import com.springbootbanking.dto.registration.RegistrationRequest;
import com.springbootbanking.dto.registration.RegistrationResponse;
import com.springbootbanking.entity.BankAccount;
import com.springbootbanking.entity.Customer;
import com.springbootbanking.exception.*;
import com.springbootbanking.repository.BankAccountRepository;
import com.springbootbanking.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Transactional
@Service
public class RegistrationService {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository,
            PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegistrationResponse register(RegistrationRequest request) {
        validateBusinessRules(request);

        Customer customer = mapToCustomer(request);

        customerRepository.save(customer);

        // Return the generated account number in response
        return new RegistrationResponse(
                true,
                "Registration successful",
                customer.getAccount().getAccountNumber());
    }

    public RegistrationResponse completeOAuthRegistration(String username, OAuthRegistrationCompleteRequest request) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found: " + username));

        int age = Period.between(request.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new MinimumAgeRequirementException("Age must be 18 or above");
        }

        Integer accountNumber = generateAccountNumber();
        BankAccount account = new BankAccount(request.getPin());
        account.setAccountNumber(accountNumber);

        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setAccount(account);
        customer.setRegistrationComplete(true);

        customerRepository.save(customer);

        return new RegistrationResponse(
                true,
                "Registration completed successfully",
                account.getAccountNumber());
    }

    private void validateBusinessRules(RegistrationRequest request) {
        validateUsernameUniqueness(request);
        validatePasswordConfirmation(request);
        validateMinimumAge(request);
    }

    private void validateUsernameUniqueness(RegistrationRequest request) {
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(
                    "Username " + request.getUsername() + " already exists");
        }
    }

    private void validatePasswordConfirmation(RegistrationRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMisMatchException("Password & confirm password do not match");
        }
    }

    private void validateMinimumAge(RegistrationRequest request) {
        int age = Period
                .between(request.getDateOfBirth(), LocalDate.now())
                .getYears();

        if (age < 18) {
            throw new MinimumAgeRequirementException("Age must be 18 or above");
        }
    }

    private Customer mapToCustomer(RegistrationRequest request) {
        // Generate account number from sequence BEFORE saving
        Integer accountNumber = generateAccountNumber();

        BankAccount account = new BankAccount(request.getPin());
        account.setAccountNumber(accountNumber);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        return new Customer(
                request.getFullName(),
                request.getUsername(),
                encodedPassword,
                request.getDateOfBirth(),
                request.getGender(),
                account);
    }

    private Integer generateAccountNumber() {
        return bankAccountRepository.getNextAccountNumber();
    }
}