package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherStockRepository extends JpaRepository<VoucherStock, Integer> {
    Optional<VoucherStock> findByVoucher(Voucher voucher);
}
