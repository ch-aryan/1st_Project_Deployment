package com.springbootbanking.service;

/*
import com.springbootbanking.dto.DepositRequest;
import com.springbootbanking.dto.DepositResponse;
import com.springbootbanking.entity.*;
import com.springbootbanking.exception.AccountInactiveException;
import com.springbootbanking.exception.AccountNotFoundException;
import com.springbootbanking.exception.InvalidPinException;
import com.springbootbanking.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    private DepositValidator validator;
    private DepositService depositService;

    @BeforeEach
    void setUp() {
        validator = new DepositValidator();
        depositService = new DepositService(validator, bankAccountRepository);
    }

    private BankAccount createMockAccount(Integer accountNumber, BigDecimal initialBalance, String pin, boolean active) {
        BankAccount account = new BankAccount(pin);
        account.setAccountNumber(accountNumber);
        account.setBalance(initialBalance);

        Customer customer = new Customer(
                "Jane Doe",
                "janedoe",
                "hashedPass",
                LocalDate.of(1992, 4, 10),
                Gender.FEMALE,
                account
        );
        customer.setActive(active);
        account.setCustomer(customer);
        return account;
    }

    @Nested
    @DisplayName("Deposit Success Tests")
    class DepositSuccessTests {

        @Test
        @DisplayName("Should successfully deposit amount, update balance, and record transaction")
        void testDeposit_Success() {
            Integer accountNumber = 100001;
            BigDecimal initialBalance = new BigDecimal("1000.00");
            BigDecimal depositAmount = new BigDecimal("500.00");
            String pin = "1234";

            BankAccount account = createMockAccount(accountNumber, initialBalance, pin, true);
            DepositRequest request = new DepositRequest(accountNumber, depositAmount, pin);

            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

            DepositResponse response = depositService.deposit(request);

            assertNotNull(response);
            assertTrue(response.isStatus());
            assertEquals(new BigDecimal("1500.00"), response.getBalance());
            assertEquals(new BigDecimal("1500.00"), account.getBalance());

            // Verify transaction was added to the account
            assertEquals(1, account.getTransactions().size());
            Transaction recordedTx = account.getTransactions().get(0);
            assertEquals(TransactionType.DEPOSIT, recordedTx.getType());
            assertEquals(depositAmount, recordedTx.getAmount());
            assertEquals(new BigDecimal("1500.00"), recordedTx.getBalanceAfter());
            assertEquals("Cash Deposit", recordedTx.getDescription());
        }
    }

    @Nested
    @DisplayName("Deposit Failure & Validation Tests")
    class DepositFailureTests {

        @Test
        @DisplayName("Should throw AccountNotFoundException when account number does not exist")
        void testDeposit_AccountNotFound() {
            Integer accountNumber = 999999;
            DepositRequest request = new DepositRequest(accountNumber, new BigDecimal("100.00"), "1234");

            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

            AccountNotFoundException ex = assertThrows(
                    AccountNotFoundException.class,
                    () -> depositService.deposit(request)
            );

            assertEquals("Account not found: 999999", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw AccountNotFoundException when account has no associated customer")
        void testDeposit_OrphanAccount() {
            Integer accountNumber = 100001;
            BankAccount orphanAccount = new BankAccount("1234");
            orphanAccount.setAccountNumber(accountNumber);
            orphanAccount.setBalance(new BigDecimal("500.00"));
            orphanAccount.setCustomer(null);

            DepositRequest request = new DepositRequest(accountNumber, new BigDecimal("100.00"), "1234");
            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(orphanAccount));

            AccountNotFoundException ex = assertThrows(
                    AccountNotFoundException.class,
                    () -> depositService.deposit(request)
            );

            assertEquals("Account exists but has no associated customer record", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw AccountInactiveException when customer account is inactive or locked")
        void testDeposit_InactiveAccount() {
            Integer accountNumber = 100001;
            BankAccount inactiveAccount = createMockAccount(accountNumber, new BigDecimal("500.00"), "1234", false);

            DepositRequest request = new DepositRequest(accountNumber, new BigDecimal("100.00"), "1234");
            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(inactiveAccount));

            AccountInactiveException ex = assertThrows(
                    AccountInactiveException.class,
                    () -> depositService.deposit(request)
            );

            assertEquals("Account is inactive or locked", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw InvalidPinException when PIN is incorrect")
        void testDeposit_IncorrectPin() {
            Integer accountNumber = 100001;
            BankAccount account = createMockAccount(accountNumber, new BigDecimal("500.00"), "1234", true);

            DepositRequest request = new DepositRequest(accountNumber, new BigDecimal("100.00"), "9999"); // Wrong PIN
            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

            InvalidPinException ex = assertThrows(
                    InvalidPinException.class,
                    () -> depositService.deposit(request)
            );

            assertEquals("Incorrect PIN", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when deposit amount is zero or negative")
        void testDeposit_ZeroOrNegativeAmount() {
            Integer accountNumber = 100001;
            BankAccount account = createMockAccount(accountNumber, new BigDecimal("500.00"), "1234", true);

            DepositRequest zeroRequest = new DepositRequest(accountNumber, BigDecimal.ZERO, "1234");
            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> depositService.deposit(zeroRequest)
            );
        }
    }
}
*/
