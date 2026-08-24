package com.springbootbanking.security;

import com.springbootbanking.entity.Customer;
import com.springbootbanking.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .or(() -> customerRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));

        return new User(
                customer.getUsername(),
                customer.getPassword(),
                customer.isActive(),
                true,
                true,
                !customer.isAccountLocked(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

