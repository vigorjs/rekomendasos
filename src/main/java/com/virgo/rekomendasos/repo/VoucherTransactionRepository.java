package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherTransactionRepository extends JpaRepository<VoucherTransaction, Integer> {
    Page<VoucherTransaction> findAll(Specification<VoucherTransaction> spec, Pageable pageable);
}
