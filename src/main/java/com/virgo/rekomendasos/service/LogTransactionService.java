package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.LogTransaction;
import com.virgo.rekomendasos.utils.dto.LogTransactionDto;

import java.util.List;

public interface LogTransactionService {
    LogTransaction create(LogTransactionDto obj);

    List<LogTransaction> findAll();

    LogTransaction findById(Long id);

    LogTransaction update(Long id, LogTransactionDto obj);

    void deleteById(Long id);

    LogTransaction findUserTransactionById(Long id);

    List<LogTransaction> findAllUserTransactions();

}
