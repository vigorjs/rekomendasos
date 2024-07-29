package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.utils.dto.restClientDto.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    public CloudinaryResponse uploadFile(MultipartFile file, String fileName);
}
