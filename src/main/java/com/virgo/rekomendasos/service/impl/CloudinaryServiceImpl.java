package com.virgo.rekomendasos.service.impl;

import com.cloudinary.Cloudinary;
import com.virgo.rekomendasos.config.advisers.exception.InternalException;
import com.virgo.rekomendasos.service.CloudinaryService;
import com.virgo.rekomendasos.utils.response.CloudinaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Transactional
    @Override
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName){
        try {
            final Map result = cloudinary.uploader().upload(file.getBytes(), Map.of("public_id", "user/profile/" + fileName));
            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder()
                    .publicId(publicId)
                    .url(url)
                    .build();

        } catch (Exception e) {
            throw new InternalException("Failed to upload File");
        }
    }
}
