package com.mile.aws.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {

    private static String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
    private static String AWS_SECRET_ACCESS_KEY = "aws.secretAccessKey";

    private final String accessKey;
    private final String secretKey;
    private final String regionString;

    public AwsConfig(@Value("${aws-property.access-key}") final String accessKey,
                     @Value("${aws-property.secret-key}") final String secretKey,
                     @Value("${aws-property.aws-region}") final String regionString) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.regionString = regionString;
    }

    @Bean
    public SystemPropertyCredentialsProvider systemPropertyCredentialsProvider() {
        System.setProperty(AWS_ACCESS_KEY_ID, accessKey);
        System.setProperty(AWS_SECRET_ACCESS_KEY, secretKey);
        return SystemPropertyCredentialsProvider.create();
    }

    @Bean
    public Region getRegion() {
        return Region.of(regionString);
    }

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .region(getRegion())
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }

    @Bean
    public S3Presigner getS3PreSigner() {
        return S3Presigner.builder()
                .region(getRegion())
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }
}