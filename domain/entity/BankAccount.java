package com.springbootbanking.domain.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private final Integer accountNumber;
    private final String accountHolderName;
    private final Date createdAt;
    private final Integer pin;
    private final ArrayList<Transaction> transactionHistory;

    private double balance;

    public BankAccount(Integer accountNumber,
                       String accountHolderName,
                       Integer pin) {

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.pin = pin;
        this.createdAt = new Date(System.currentTimeMillis());

        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();//Every account automatically starts with an empty transaction history.Exactly like a real bank account.
    }

    // ----------------------------
    // Business Methods
    // ----------------------------

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public boolean verifyPin(int enteredPin) {
        return pin.equals(enteredPin);
    }

    public void addTransaction(Transaction transaction)
    {
        transactionHistory.add(transaction);
    }

    public List<Transaction> getTransactionHistory()
    {
        return transactionHistory;
    }
    /*
    I intentionally return List<Transaction> instead of ArrayList<Transaction>
Why? Because callers only need to read the history.They don't need to know the implementation is an ArrayList.
Programming to the interface (List) rather than the implementation (ArrayList) is a good habit and gives us flexibility later.
     */

    // ----------------------------
    // Getters
    // ----------------------------

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return """
                -----------------------------
                Account Number : %d
                Holder Name    : %s
                Balance        : %.2f
                Created At     : %s
                -----------------------------
                """
                .formatted(
                        accountNumber,
                        accountHolderName,
                        balance,
                        createdAt
                );
    }
}




/*
 @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber=" + accountNumber +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", createdAt=" + createdAt +
                ", balance=" + balance +
                '}';
    }
    OOP Relationship

Our ownership becomes

User
   ♦
   │
BankAccount
      ♦
      │
ArrayList<Transaction>
              ♦
              │
Transaction

This is composition all the way down:

A User owns a BankAccount.
A BankAccount owns its transaction history.
A Transaction cannot exist in our system without belonging to an account.
 */
