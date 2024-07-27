package com.virgo.rekomendasos.utils.dto.restClientDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MidtransResponseDTO {

    @JsonProperty("status_code")
    private String status_code;

    @JsonProperty("status_message")
    private String status_message;

    @JsonProperty("transaction_id")
    private String transaction_id;

    @JsonProperty("order_id")
    private String order_id;

    @JsonProperty("merchant_id")
    private String merchant_id;

    @JsonProperty("gross_amount")
    private String gross_amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment_type")
    private String payment_type;

    @JsonProperty("transaction_time")
    private String transaction_time;

    @JsonProperty("transaction_status")
    private String transaction_status;

    @JsonProperty("fraud_status")
    private String fraud_status;

    @JsonProperty("va_numbers")
    private List<VaNumbers> va_numbers;

    @JsonProperty("expiry_time")
    private String expiry_time;


    public static class VaNumbers{
        @JsonProperty("bank")
        private String bank;

        @JsonProperty("va_number")
        private String va_number;
    }
}
