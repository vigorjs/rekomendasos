package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.enums.TransactionStatus;
import com.virgo.rekomendasos.model.meta.LogTransaction;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.LogTransactionsRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.LogTransactionService;
import com.virgo.rekomendasos.utils.dto.LogTransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LogTransactionServiceImpl implements LogTransactionService {

    @Autowired
    private LogTransactionsRepository logTransactionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public LogTransaction create(LogTransactionDto obj) {
        User user = userRepository.findById(obj.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        LogTransaction logTransaction = LogTransaction.builder()
                .orderId(obj.getOrderId())
                .grossAmount(obj.getGrossAmount())
                .user(user)
                .status(TransactionStatus.valueOf(obj.getStatus()))
                .build();
        return logTransactionsRepository.save(logTransaction);
    }

    @Override
    public List<LogTransaction> findAll() {
        return logTransactionsRepository.findAll();
    }

    @Override
    public LogTransaction findByOrderId(String orderId) {
        Optional<LogTransaction> logTransaction = logTransactionsRepository.findByOrderId(orderId); // <>
        return logTransaction.orElse(null);
    }

    @Override
    public LogTransaction update(String orderId, LogTransactionDto obj) {
        Optional<LogTransaction> logTransactionOptional = logTransactionsRepository.findByOrderId(orderId); // <>
        User user = userRepository.findById(obj.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (logTransactionOptional.isEmpty()) {
            return null;
        }

        LogTransaction logTransaction = logTransactionOptional.get();
        logTransaction.setGrossAmount(obj.getGrossAmount());
        logTransaction.setUser(user);
        logTransaction.setStatus(TransactionStatus.valueOf(obj.getStatus()));
        return logTransactionsRepository.save(logTransaction);
    }

    @Override
    public void deleteById(String orderId) {
        Optional<LogTransaction> logTransactionOptional = logTransactionsRepository.findByOrderId(orderId); // <>
        if (logTransactionOptional.isEmpty()) {
            return;
        }
        logTransactionsRepository.delete(logTransactionOptional.get());
    }

    @Override
    public LogTransaction findUserTransactionById(String orderId) {
        Optional<LogTransaction> logTransaction = logTransactionsRepository.findByOrderId(orderId); // <>
        return logTransaction.orElse(null);
    }

    @Override
    public List<LogTransaction> findAllUserTransactions() {
        User user = authenticationService.getUserAuthenticated();
        if (user == null) {
            return null;
        }
        List<LogTransaction> logTransactions = logTransactionsRepository.findAll();
        List<LogTransaction> result = new ArrayList<>();
        for (LogTransaction logTransaction : logTransactions) {
            if (Objects.equals(logTransaction.getUser().getId(), user.getId())) {
                result.add(logTransaction);
            }
        }
        return result;
    }
}
