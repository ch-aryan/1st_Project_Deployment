package com.springbootbanking.repository;

import com.springbootbanking.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByAccountNumber(Integer accountNumber);

    @Query(value = "SELECT nextval('account_number_seq')", nativeQuery = true)
    Integer getNextAccountNumber();
}
