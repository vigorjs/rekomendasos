package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.enums.TransactionStatus;
import com.virgo.rekomendasos.model.meta.LogTransaction;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.LogTransactionsRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.LogTransactionService;
import com.virgo.rekomendasos.service.MidtransService;
import com.virgo.rekomendasos.service.UserService;
import com.virgo.rekomendasos.utils.dto.LogTransactionDto;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class LogTransactionServiceImpl implements LogTransactionService {

    private final LogTransactionsRepository logTransactionsRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final MidtransService midtransService;
    private final UserService userService;
    private final ExecutorService executorService;

    public LogTransactionServiceImpl(LogTransactionsRepository logTransactionsRepository, UserRepository userRepository, AuthenticationService authenticationService, MidtransService midtransService, @Lazy UserService userService, ExecutorService executorService) {
        this.logTransactionsRepository = logTransactionsRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.midtransService = midtransService;
        this.userService = userService;
        this.executorService = executorService;
    }

    @Override
    @Transactional
    public LogTransaction create(LogTransactionDto obj) {
        User user = userRepository.findById(obj.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        LogTransaction logTransaction = LogTransaction.builder()
                .order_id(obj.getOrderId())
                .gross_amount(obj.getGrossAmount())
                .user(user)
                .status(TransactionStatus.valueOf(obj.getStatus()))
                .build();
        LogTransaction save = logTransactionsRepository.save(logTransaction);

        executorService.submit( () -> updateLogTransactionStatus(save.getId(), obj) );

        return logTransaction;
    }

    @Override
    public List<LogTransaction> findAll() {
        return logTransactionsRepository.findAll();
    }

    @Override
    public LogTransaction findById(Long id) {
        Optional<LogTransaction> logTransaction = logTransactionsRepository.findById(id); // <>
        return logTransaction.orElse(null);
    }

    @Override
    public LogTransaction update(Long id, LogTransactionDto obj) {
        Optional<LogTransaction> logTransactionOptional = logTransactionsRepository.findById(id); // <>
        User user = userRepository.findById(obj.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (logTransactionOptional.isEmpty()) {
            return null;
        }

        LogTransaction logTransaction = logTransactionOptional.get();
        logTransaction.setGross_amount(obj.getGrossAmount());
        logTransaction.setUser(user);
        logTransaction.setStatus(TransactionStatus.valueOf(obj.getStatus()));
        return logTransactionsRepository.save(logTransaction);
    }

    @Override
    public void deleteById(Long id) {
        Optional<LogTransaction> logTransactionOptional = logTransactionsRepository.findById(id); // <>
        if (logTransactionOptional.isEmpty()) {
            return;
        }
        logTransactionsRepository.delete(logTransactionOptional.get());
    }

    @Override
    public LogTransaction findUserTransactionById(Long id) {
        Optional<LogTransaction> logTransaction = logTransactionsRepository.findById(id); // <>
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


    public void updateLogTransactionStatus(Long id, LogTransactionDto obj) {
        boolean flag = false;
        for (int i = 0; i < 120; i++) {
            try {
                MidtransResponseDTO response = midtransService.getStatus(obj.getOrderId());

                if (response != null && String.valueOf(TransactionStatus.settlement).equals(response.getTransaction_status())) {
                    LogTransaction logTransaction = findById(id);
                    logTransaction.setStatus(TransactionStatus.settlement);
                    logTransactionsRepository.save(logTransaction);
                    userService.updateBalance(logTransaction.getUser(), logTransaction.getGross_amount());
                    flag = true;
                    break;
                }
                Thread.sleep(3000);
            } catch (Exception e) {
                log.error("error in updateTransactionStatus() {}", e.getMessage());
            }
        }
        log.info("Exiting updateTransactionStatus()");
        if (!flag){
            midtransService.changeStatus(obj.getOrderId(), String.valueOf(TransactionStatus.failure));
        }
    }
}
