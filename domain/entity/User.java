package com.springbootbanking.domain.entity;


import java.time.LocalDate;

public class User {
    private final Integer id;
    private  final String fullName;
    private final LocalDate dob;
    private final String userName;
    private final String password;
    private  final BankAccount account;

    public User(Integer id, String userName, String password, String fullName , BankAccount account, LocalDate dob) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.account = account;
        this.dob = dob;

    }


    public String getFullName(){
        return fullName;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public BankAccount getBankAccount() {
        return account;
    }

    public LocalDate getDob() {
        return dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", account=" + account +
                ", dob=" + dob +
                ", fullName='" + fullName + '\'' +
                ", id=" + id +
                '}';
    }
}
