package com.virgo.rekomendasos.utils.dto.restClientDto;

import jakarta.annotation.Nullable;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MidtransRequestDTO {

    @Nullable
    private String payment_type = "bank_transfer";

    @Nullable
    private TransactionDetails transaction_details;

    private BankTransfer bank_transfer;

    @Nullable
    private CustomExpiry custom_expiry = new CustomExpiry();

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TransactionDetails {
        private String order_id;
        private Integer gross_amount;
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
        @Nullable
        private String unit = "minute";

        @Nullable
        private Integer expiry_duration = 60;

        @Nullable
        private Long order_time;

        @PrePersist
        void onCreate() {
            order_time = Instant.now().toEpochMilli();
        }
    }


}
