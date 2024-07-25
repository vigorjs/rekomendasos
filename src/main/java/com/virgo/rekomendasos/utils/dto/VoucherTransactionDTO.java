package com.virgo.rekomendasos.utils.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherTransactionDTO {
    @NotBlank(message = "User must no be blank")
    private Integer userId;

    @NotBlank(message = "Voucher must no be blank")
    private Integer voucherId;

    @NotBlank(message = "Voucher must no be blank")
    @Min(value = 0, message = "Voucher Quantity cannot be a negative value")
    private Integer voucherQuantity;
}