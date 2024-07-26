package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>, JpaSpecificationExecutor<Voucher> {
}
