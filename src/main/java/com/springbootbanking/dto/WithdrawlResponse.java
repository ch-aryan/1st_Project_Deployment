package com.springbootbanking.dto;

import java.math.BigDecimal;

public class WithdrawlResponse {

    private boolean success;

    private String message;

    private BigDecimal balance;

    public WithdrawlResponse(boolean success,
                             String message,
                             BigDecimal balance){

        this.success = success;
        this.message = message;
        this.balance = balance;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public BigDecimal getBalance(){
        return balance;
    }

}