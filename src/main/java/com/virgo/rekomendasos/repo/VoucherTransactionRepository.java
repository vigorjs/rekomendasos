package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherTransactionRepository extends JpaRepository<VoucherTransaction, Integer>, JpaSpecificationExecutor<VoucherTransaction> {
}
