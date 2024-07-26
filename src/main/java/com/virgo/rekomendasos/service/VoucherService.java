package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherService {
    Voucher create(VoucherDTO newVoucher);
    Page<Voucher> findAll(Pageable pageable);
    Page<Voucher> findAllVoucherByUserId(Pageable pageable);
    Voucher findById(Integer id);
    Voucher findByUserIdAndId(Integer id);
    Voucher updateById(Integer id, VoucherDTO updatedVoucher);
    void deleteById(Integer id);
}
