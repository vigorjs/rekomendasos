package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransRequestDTO;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransResponseDTO;

public interface MidtransService {
    MidtransResponseDTO chargePayment(MidtransRequestDTO req);

    MidtransResponseDTO getStatus(String order_id);

    MidtransResponseDTO changeStatus(String order_id, String status);
}
