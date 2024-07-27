package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.LogTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LogTransactionsRepository extends JpaRepository<LogTransaction, Long> {

    @Query("SELECT lt FROM LogTransaction lt ORDER BY lt.id DESC LIMIT 1")
    Optional<LogTransaction> findTopByOrderByIdDesc();
}
