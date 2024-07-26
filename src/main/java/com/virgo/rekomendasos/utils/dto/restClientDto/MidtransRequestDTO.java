package com.virgo.rekomendasos.utils.dto.restClientDto;

import jakarta.annotation.Nullable;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MidtransRequestDTO {

    @Nullable
    private String payment_type;

    private TransactionDetails transaction_details;

    private BankTransfer bank_transfer;

    @Nullable
    private CustomExpiry custom_expiry;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TransactionDetails {
        @Nullable
        private String order_id;

        @Min(value = 10000, message = "Min Topup 10.000")
        private Long gross_amount;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class BankTransfer {
        private String bank;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomExpiry {
        private String unit;
        private Integer expiry_duration;
        private String order_time;
    }


}
