package com.virgo.rekomendasos.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.virgo.rekomendasos.service.impl.CloudinaryServiceImpl;
import com.virgo.rekomendasos.utils.dto.restClientDto.CloudinaryResponse;
import com.virgo.rekomendasos.config.advisers.exception.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceTest {
    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    private String fileName;
    private byte[] fileBytes;
    private Map<String, String> resultMap;

    @BeforeEach
    void setUp() throws IOException {
        // Initialize common test data
        fileName = "testFile";
        fileBytes = "file content".getBytes();

        // Mock the behavior of getting bytes from the MultipartFile
        when(file.getBytes()).thenReturn(fileBytes);

        // Initialize result map that will be returned by Cloudinary's upload method
        resultMap = Map.of(
                "secure_url", "http://example.com/testFile",
                "public_id", "user/profile/testFile"
        );

        // Mock the behavior of the Cloudinary uploader's upload method with lenient
        lenient().when(cloudinary.uploader()).thenReturn(uploader);
        lenient().when(uploader.upload(fileBytes, ObjectUtils.asMap("public_id", "user/profile/" + fileName)))
                .thenReturn(resultMap);
    }

    @Test
    void uploadFile_Success() throws IOException {
        // When
        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);

        // Then
        assertEquals("user/profile/testFile", response.getPublicId());
        assertEquals("http://example.com/testFile", response.getUrl());
    }

    @Test
    void uploadFile_Failure() throws IOException {
        // Given
        // Overriding the previous setup for failure case
        when(file.getBytes()).thenThrow(new IOException("File upload error"));

        // When & Then
        assertThrows(InternalException.class, () -> cloudinaryService.uploadFile(file, fileName));
    }
}
