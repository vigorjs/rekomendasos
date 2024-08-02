package com.virgo.rekomendasos.model.meta;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Data
@Table(name = "voucher_stock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoucherStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    @Column(name = "quantity", nullable = false)
    @Min(value = 0, message = "qty tidak boleh minus")
    private Integer quantity;
}
