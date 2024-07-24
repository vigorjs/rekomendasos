package com.virgo.rekomendasos.utils.dto;

import com.virgo.rekomendasos.model.meta.VoucherTransaction;

public class VoucherTransactionConvert {
    public static VoucherTransactionDTO toDTO(VoucherTransaction voucherTransaction) {
        return VoucherTransactionDTO.builder()
                .userId(voucherTransaction.getUser().getId())
                .voucherId(voucherTransaction.getVoucher().getId())
                .voucherQuantity(voucherTransaction.getVoucherQuantity() > 0 ? voucherTransaction.getVoucherQuantity() : 0)
                .build();
    }
}
