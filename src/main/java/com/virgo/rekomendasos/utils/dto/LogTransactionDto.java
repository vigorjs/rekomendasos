package com.virgo.rekomendasos.utils.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogTransactionDto {

    private String orderId;

    private Long grossAmount;

    private String status;

    private Integer userId;

}
