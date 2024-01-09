package com.mile.controller.external;

import com.mile.aws.utils.PreSignedUrlResponse;
import com.mile.aws.utils.S3BucketDirectory;
import com.mile.aws.utils.S3Service;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class S3Controller implements S3ControllerSwagger {
    private final S3Service s3Service;

    @GetMapping("/image/upload")
    @Override
    public SuccessResponse<PreSignedUrlResponse> getPreSignedUrl() {
        return SuccessResponse.of(SuccessMessage.PRESIGNED_URL_GET_SUCCESS, s3Service.getUploadPreSignedUrl(S3BucketDirectory.POST_PREFIX));
    }
}
