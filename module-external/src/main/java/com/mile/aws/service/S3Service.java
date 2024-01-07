package com.mile.aws.service;


import com.mile.aws.config.AwsConfig;
import com.mile.aws.utils.S3BucketDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class S3Service {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");
    private static final Long MAX_FILE_SIZE = 5 * 1024 * 1024L;

    private static final Long PRE_SIGNED_URL_EXPIRE_MINUTE = 1L;  // 만료시간 1분

    private final String bucketName;
    private final AwsConfig awsConfig;

    public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName, AwsConfig awsConfig) {
        this.bucketName = bucketName;
        this.awsConfig = awsConfig;
    }

    // Multipart 요청을 통한 이미지 업로드
    public String uploadImage(final S3BucketDirectory directoryPath,
                              final MultipartFile image) throws IOException {
        validateExtension(image);
        validateFileSize(image);

        final String key = directoryPath.value() + generateImageFileName();

        try {
            final S3Client s3Client = awsConfig.getS3Client();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(image.getContentType())
                    .contentDisposition("inline").build();

            RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
            s3Client.putObject(request, requestBody);
            return key;
        } catch (RuntimeException e) {
            throw new BusinessException(FAIL_TO_UPLOAD_IMAGE);
        }
    }


    // PreSigned Url을 통한 이미지 업로드
    public PreSignedUrlResponse getUploadPreSignedUrl(final S3BucketDirectory prefix) throws IOException {
        final String fileName = generateImageFileName();   // UUID 문자열
        final String key = prefix.value() + fileName;

        log.info("S3 세팅 성공!: {}", key);
        log.info("업로드할 image 경로: {}", prefix);

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
            throw new BusinessException(FAIL_TO_UPLOAD_IMAGE);
        }
    }

    // S3 버킷에 업로드된 이미지 삭제
    public void deleteImage(String key) throws IOException {
        try {
            final S3Client s3Client = awsConfig.getS3Client();

            s3Client.deleteObject((DeleteObjectRequest.Builder builder) ->
                    builder.bucket(bucketName)
                            .key(key).build());
        } catch (RuntimeException e) {
            throw new BusinessException(FAIL_TO_DELETE_IMAGE);
        }
    }

    private String generateImageFileName() {
        return UUID.randomUUID().toString() + ".jpg";
    }

    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new RuntimeException("이미지 확장자는 jpg, png, webp만 가능합니다.");
        }
    }

    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("이미지 사이즈는 5MB를 넘을 수 없습니다.");
        }
    }
}