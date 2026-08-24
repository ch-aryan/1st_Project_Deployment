package com.springbootbanking.service;

import com.springbootbanking.dto.WithdrawRequest;
import com.springbootbanking.dto.WithdrawlResponse;
import com.springbootbanking.entity.*;
import com.springbootbanking.exception.AccountInactiveException;
import com.springbootbanking.exception.AccountNotFoundException;
import com.springbootbanking.exception.InsufficientBalanceException;
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
public class WithdrawServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    private WithdrawValidator validator;
    private WithdrawService withdrawService;

    @BeforeEach
    void setUp() {
        validator = new WithdrawValidator();
        withdrawService = new WithdrawService(validator, bankAccountRepository);
    }

    private BankAccount createMockAccount(Integer accountNumber, BigDecimal initialBalance, String pin, boolean active) {
        BankAccount account = new BankAccount(pin);
        account.setAccountNumber(accountNumber);
        account.setBalance(initialBalance);

        Customer customer = new Customer(
                "Robert Johnson",
                "robertj",
                "hashedPass",
                LocalDate.of(1988, 11, 25),
                Gender.MALE,
                account
        );
        customer.setActive(active);
        account.setCustomer(customer);
        return account;
    }

    @Nested
    @DisplayName("Withdrawal Success Tests")
    class WithdrawalSuccessTests {

        @Test
        @DisplayName("Should successfully withdraw amount, deduct balance, and record transaction")
        void testWithdraw_Success() {
            Integer accountNumber = 100001;
            BigDecimal initialBalance = new BigDecimal("2000.00");
            BigDecimal withdrawAmount = new BigDecimal("750.00");
            String pin = "1234";

            BankAccount account = createMockAccount(accountNumber, initialBalance, pin, true);
            WithdrawRequest request = new WithdrawRequest(accountNumber, withdrawAmount, pin);

            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

            WithdrawlResponse response = withdrawService.withdraw(request);

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals(new BigDecimal("1250.00"), response.getBalance());
            assertEquals(new BigDecimal("1250.00"), account.getBalance());

            // Verify withdrawal transaction was recorded
            assertEquals(1, account.getTransactions().size());
            Transaction recordedTx = account.getTransactions().get(0);
            assertEquals(TransactionType.WITHDRAW, recordedTx.getType());
            assertEquals(withdrawAmount, recordedTx.getAmount());
            assertEquals(new BigDecimal("1250.00"), recordedTx.getBalanceAfter());
            assertEquals("Cash Withdrawal", recordedTx.getDescription());
        }

        @Test
        @DisplayName("Should successfully withdraw entire balance to reach zero")
        void testWithdraw_EntireBalance() {
            Integer accountNumber = 100001;
            BigDecimal initialBalance = new BigDecimal("500.00");
            BigDecimal withdrawAmount = new BigDecimal("500.00");
            String pin = "1234";

            BankAccount account = createMockAccount(accountNumber, initialBalance, pin, true);
            WithdrawRequest request = new WithdrawRequest(accountNumber, withdrawAmount, pin);

            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

            WithdrawlResponse response = withdrawService.withdraw(request);

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals(0, BigDecimal.ZERO.compareTo(response.getBalance()));
            assertEquals(0, BigDecimal.ZERO.compareTo(account.getBalance()));
        }
    }

    @Nested
    @DisplayName("Withdrawal Failure & Validation Tests")
    class WithdrawalFailureTests {

        @Test
        @DisplayName("Should throw AccountNotFoundException when account number does not exist")
        void testWithdraw_AccountNotFound() {
            Integer accountNumber = 999999;
            WithdrawRequest request = new WithdrawRequest(accountNumber, new BigDecimal("100.00"), "1234");

            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

            AccountNotFoundException ex = assertThrows(
                    AccountNotFoundException.class,
                    () -> withdrawService.withdraw(request)
            );

            assertEquals("Account not found: 999999", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw AccountNotFoundException when account has no customer record")
        void testWithdraw_OrphanAccount() {
            Integer accountNumber = 100001;
            BankAccount orphanAccount = new BankAccount("1234");
            orphanAccount.setAccountNumber(accountNumber);
            orphanAccount.setBalance(new BigDecimal("1000.00"));
            orphanAccount.setCustomer(null);

            WithdrawRequest request = new WithdrawRequest(accountNumber, new BigDecimal("100.00"), "1234");
            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(orphanAccount));

            AccountNotFoundException ex = assertThrows(
                    AccountNotFoundException.class,
                    () -> withdrawService.withdraw(request)
            );

            assertEquals("Account exists but has no associated customer record", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw AccountInactiveException when account is inactive")
        void testWithdraw_InactiveAccount() {
            Integer accountNumber = 100001;
            BankAccount inactiveAccount = createMockAccount(accountNumber, new BigDecimal("1000.00"), "1234", false);

            WithdrawRequest request = new WithdrawRequest(accountNumber, new BigDecimal("100.00"), "1234");
            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(inactiveAccount));

            AccountInactiveException ex = assertThrows(
                    AccountInactiveException.class,
                    () -> withdrawService.withdraw(request)
            );

            assertEquals("Account is inactive or locked", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw InvalidPinException when PIN is incorrect")
        void testWithdraw_IncorrectPin() {
            Integer accountNumber = 100001;
            BankAccount account = createMockAccount(accountNumber, new BigDecimal("1000.00"), "1234", true);

            WithdrawRequest request = new WithdrawRequest(accountNumber, new BigDecimal("100.00"), "0000"); // Wrong PIN
            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

            InvalidPinException ex = assertThrows(
                    InvalidPinException.class,
                    () -> withdrawService.withdraw(request)
            );

            assertEquals("Incorrect PIN", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw InsufficientBalanceException when withdrawal amount exceeds available balance")
        void testWithdraw_InsufficientBalance() {
            Integer accountNumber = 100001;
            BigDecimal initialBalance = new BigDecimal("300.00");
            BigDecimal withdrawAmount = new BigDecimal("500.00"); // Exceeds balance
            String pin = "1234";

            BankAccount account = createMockAccount(accountNumber, initialBalance, pin, true);
            WithdrawRequest request = new WithdrawRequest(accountNumber, withdrawAmount, pin);

            when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

            InsufficientBalanceException ex = assertThrows(
                    InsufficientBalanceException.class,
                    () -> withdrawService.withdraw(request)
            );

            assertTrue(ex.getMessage().contains("Insufficient balance"));
            assertEquals(initialBalance, ex.getAvailableBalance());
            assertEquals(withdrawAmount, ex.getRequestedAmount());

            // Verify account balance was unchanged and no transaction was recorded
            assertEquals(new BigDecimal("300.00"), account.getBalance());
            assertTrue(account.getTransactions().isEmpty());
        }
    }
}

