package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherService {
    Voucher create(VoucherDTO newVoucher);
    Page<Voucher> getAll(Pageable pageable);
    Voucher getById(Integer id);
    Voucher updateById(Integer id, VoucherDTO updatedVoucher);
    void deleteById(Integer id);
    Voucher use(Integer id, Integer quantity);
}
