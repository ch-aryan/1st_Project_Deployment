package com.springbootbanking.dto;

import com.springbootbanking.entity.Transaction;
import java.util.List;

public record TransactionHistoryResponse(boolean success, String message, List<Transaction> transactions) {}
