package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.LogTransaction;
import com.virgo.rekomendasos.utils.dto.LogTransactionDto;

import java.util.List;

public interface LogTransactionService {
    LogTransaction create(LogTransactionDto obj);

    List<LogTransaction> findAll();

    LogTransaction findByOrderId(String orderId);

    LogTransaction update(String orderId, LogTransactionDto obj);

    void deleteById(String orderId);

    LogTransaction findUserTransactionById(String orderId);

    List<LogTransaction> findAllUserTransactions();
}
