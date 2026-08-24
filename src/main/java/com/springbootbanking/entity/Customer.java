package com.springbootbanking.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer owns the relationship: has @JoinColumn, cascade ALL, orphanRemoval true
    // BankAccount can be null until an OAuth user completes registration
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", nullable = true, unique = true)
    private BankAccount account;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "dob", nullable = true)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    // PIN moved to BankAccount - single source of truth for transaction auth
    // Customer no longer stores PIN

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'LOCAL'")
    private AuthProvider authProvider = AuthProvider.LOCAL;

    @Column(name = "oauth_provider_id")
    private String oauthProviderId;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean registrationComplete = true;

    private boolean active;
    private int failedLoginAttempts;
    private boolean accountLocked;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Customer() {
        // required by hibernate/JPA
    }

    public Customer(String fullName, String username, String password, LocalDate dateOfBirth, Gender gender, BankAccount account) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
        this.updatedAt = LocalDateTime.now();
        this.authProvider = AuthProvider.LOCAL;
        this.registrationComplete = true;
        this.account = account;
        if (account != null) {
            account.setCustomer(this);
        }
    }

    // Constructor for OAuth registration
    public Customer(String fullName, String username, String email, AuthProvider authProvider, String oauthProviderId, String encodedPassword) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.authProvider = authProvider;
        this.oauthProviderId = oauthProviderId;
        this.password = encodedPassword;
        this.active = true;
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
        this.registrationComplete = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
        if (account != null) {
            account.setCustomer(this);
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public String getOauthProviderId() {
        return oauthProviderId;
    }

    public void setOauthProviderId(String oauthProviderId) {
        this.oauthProviderId = oauthProviderId;
    }

    public boolean isRegistrationComplete() {
        return registrationComplete;
    }

    public void setRegistrationComplete(boolean registrationComplete) {
        this.registrationComplete = registrationComplete;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (failedLoginAttempts >= 5) {
            this.accountLocked = true;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void resetFailedLogins() {
        this.failedLoginAttempts = 0;
        this.updatedAt = LocalDateTime.now();
    }
}
