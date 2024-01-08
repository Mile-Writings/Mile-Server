package com.mile.controller;

import com.mile.aws.utils.PreSignedUrlResponse;
import com.mile.aws.utils.S3BucketDirectory;
import com.mile.aws.utils.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final S3Service s3Service;

    @PostMapping()
    public void testImage(@RequestBody MultipartFile image) throws IOException {
        s3Service.uploadImage(S3BucketDirectory.TEST_PREFIX, image);
    }

    @GetMapping
    public PreSignedUrlResponse getPresignedUrl() {
        return s3Service.getUploadPreSignedUrl(S3BucketDirectory.TEST_PREFIX);
    }
}
