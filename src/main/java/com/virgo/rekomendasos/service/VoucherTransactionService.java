package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherTransactionService {
    VoucherTransaction create(VoucherTransactionDTO newVoucherTransaction);
    Page<VoucherTransaction> getAll(Pageable pageable);
    Page<VoucherTransaction> getAllByUserId(Pageable pageable, Integer userId);
    VoucherTransaction getById(Integer id);
    VoucherTransaction updateById(Integer id, VoucherTransactionDTO updatedVoucherTransaction);
    void deleteById(Integer id);
    VoucherTransaction use(Integer id, Integer quantity);
}
