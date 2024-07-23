package com.virgo.rekomendasos.utils.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO {
    @NotBlank(message = "User must no be blank")
    private Integer user_id;

    @NotBlank(message = "Voucher must no be blank")
    private Integer voucher_id;

    @NotBlank(message = "Voucher must no be blank")
    @Min(value = 0, message = "Voucher Quantity cannot be a negative value")
    private Integer voucherQuantity;
}
