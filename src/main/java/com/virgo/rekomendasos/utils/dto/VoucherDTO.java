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
    @NotBlank(message = "Name must no be blank")
    private String name;

    @NotBlank(message = "Name must no be blank")
    @Min(value = 0, message = "Price cannot be a negative value")
    private Integer price;

    @NotBlank(message = "Name must no be blank")
    @Min(value = 0, message = "Quantity cannot be a negative value")
    private Integer quantity;
}
