package com.springbootbanking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_accounts")
@SequenceGenerator(
        name = "account_number_seq",
        sequenceName = "account_number_seq",
        allocationSize = 1
)
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, updatable = false)
    private Integer accountNumber;

    @Column(name = "balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "pin", nullable = false)
    private String pin;

    // Bidirectional One-to-One: BankAccount is the inverse side
    // Customer owns the relationship (has @JoinColumn)
    @OneToOne(mappedBy = "account")
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Transaction> transactions = new ArrayList<>();

    // Default constructor (required by JPA)
    protected BankAccount() {
    }

    // Constructor for creating new account during registration
    public BankAccount(String pin) {
        this.balance = BigDecimal.ZERO;
        this.pin = pin;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Business methods
    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public boolean verifyPin(String rawPin) {
        return this.pin.equals(rawPin); // In production: use BCrypt
    }

    // Helper method for bidirectional synchronization
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        // maintain bidirectional sync
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);

    }
}