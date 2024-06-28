package com.mile.aws.utils;


import com.mile.aws.config.AwsConfig;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.MileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class S3Service {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");
    private static final Long MAX_FILE_SIZE = 5 * 1024 * 1024L;

    private static final Long PRE_SIGNED_URL_EXPIRE_MINUTE = 120L;  // 만료시간 2시간

    private final String bucketName;
    private final AwsConfig awsConfig;

    public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName, AwsConfig awsConfig) {
        this.bucketName = bucketName;
        this.awsConfig = awsConfig;
    }


    // PreSigned Url을 통한 이미지 업로드
    public PreSignedUrlResponse getUploadPreSignedUrl(final S3BucketDirectory prefix) {
        final String fileName = generateImageFileName();   // UUID 문자열
        final String key = prefix.value() + fileName;

        try {
            final S3Presigner preSigner = awsConfig.getS3PreSigner();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key).build();

            PutObjectPresignRequest preSignedUrlRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRE_MINUTE))
                    .putObjectRequest(request).build();

            String url = preSigner.presignPutObject(preSignedUrlRequest).url().toString();
            return PreSignedUrlResponse.of(fileName, url);
        } catch (RuntimeException e) {
            throw new MileException(ErrorMessage.PRESIGNED_URL_GET_ERROR);
        }
    }

    // S3 버킷에 업로드된 이미지 삭제
    public void deleteImage(final String key) {
        try {
            final S3Client s3Client = awsConfig.getS3Client();

            s3Client.deleteObject((DeleteObjectRequest.Builder builder) ->
                    builder.bucket(bucketName)
                            .key(key).build());
        } catch (RuntimeException e) {
            throw new MileException(ErrorMessage.IMAGE_DELETE_ERROR);
        }
    }

    private String generateImageFileName() {
        return UUID.randomUUID().toString() + ".jpg";
    }
}