package com.virgo.rekomendasos.model.meta;

import com.virgo.rekomendasos.model.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "log_transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LogTransaction {
    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "gross_amount")
    private Integer grossAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;
}
