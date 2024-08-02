package com.virgo.rekomendasos.utils.dto;

import com.virgo.rekomendasos.model.meta.Voucher;

public class VoucherConvert {
    public static VoucherDTO toDTO(Voucher voucher) {
        return VoucherDTO.builder()
                .name(voucher.getName())
                .price(voucher.getPrice() > 0 ? voucher.getPrice() : 0)
                .quantity(voucher.getVoucherStock() != null ? voucher.getVoucherStock().getQuantity() : 0)
                .build();
    }
}
