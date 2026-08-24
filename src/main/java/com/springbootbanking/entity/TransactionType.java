package com.springbootbanking.entity;

public enum TransactionType {
//    DEPOSIT,
//    WITHDRAW,
//    TRANSFER,
//    RECEIVED,


    DEPOSIT("Cash Deposit"),
    WITHDRAW("Cash Withdrawal"),
    TRANSFER("Money Transferred"),
    RECEIVED("Money Received");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
/*

One Small Refactoring Suggestion (Version 1.1)
I would actually make one tiny improvement before we freeze Version 1.

Notice this line:

System.out.println("Type           : "
        + transaction.getTransactionType());
Today it prints:

DEPOSIT
WITHDRAW
TRANSFER
RECEIVED
That's fine for developers, but not ideal for users.

Instead, we can enhance our TransactionType enum:

public enum TransactionType {

    DEPOSIT("Cash Deposit"),
    WITHDRAW("Cash Withdrawal"),
    TRANSFER("Money Transferred"),
    RECEIVED("Money Received");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
Then the history becomes:

Type : Cash Deposit
Type : Money Transferred
Type : Money Received
This keeps the enum responsible for its own presentation instead of scattering display strings throughout the application.
 */