package com.springbootbanking.repository;

import com.springbootbanking.entity.AuthProvider;
import com.springbootbanking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByAuthProviderAndOauthProviderId(AuthProvider authProvider, String oauthProviderId);
}
