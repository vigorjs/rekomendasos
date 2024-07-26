package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.LogTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LogTransactionsRepository extends JpaRepository<LogTransaction, String> {
    @Query(value = "SELECT * FROM log_transactions WHERE order_id = :orderId", nativeQuery = true)
    Optional<LogTransaction> findByOrderId(String orderId);
}
