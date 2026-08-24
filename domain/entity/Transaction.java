package com.springbootbanking.domain.entity;

import java.time.LocalDateTime;

public class Transaction {

private static int nextTransactionId = 1001;
        private int transactionId;
        private TransactionType transactionType;
        private double amount;
        private double balanceAfterTransaction;
        private LocalDateTime dateTime;
        private String description;

        public Transaction(
                           TransactionType transactionType,
                           double amount,
                           String description,
          double balanceAfterTransaction)
        {
            this.transactionId = nextTransactionId++;
            this.transactionType = transactionType;
            this.amount = amount;
            this.balanceAfterTransaction = balanceAfterTransaction;
            this.dateTime = LocalDateTime.now();
            this.description = description;
        }

        public int getTransactionId()
        {
            return transactionId;
        }

        public TransactionType getTransactionType()
        {
            return transactionType;
        }

        public double getAmount()
        {
            return amount;
        }

        public LocalDateTime getDateTime()
        {
            return dateTime;
        }

        public double getBalanceAfterTransaction()
        {
        return balanceAfterTransaction;
        }

        public String getDescription()
        {
            return description;
        }

        @Override
        public String toString()
        {
            return "Transaction{" +
                    "transactionId=" + transactionId +
                    ", transactionType=" + transactionType +
                    ", amount=" + amount +
                    ", dateTime=" + dateTime +
                    ", description='" + description + '\'' +
                    ", balanceAfterTransaction=" + balanceAfterTransaction +
                    '}';

        }
    }

