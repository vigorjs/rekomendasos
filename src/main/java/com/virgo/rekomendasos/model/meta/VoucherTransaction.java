package com.virgo.rekomendasos.model.meta;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "voucher_transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoucherTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    @Column(name = "voucher_quantity", nullable = false)
    private Integer voucherQuantity;
}
