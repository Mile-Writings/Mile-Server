package com.mile.aws.utils;

public record PreSignedUrlResponse(
        String fileName,
        String url
) {

    public static PreSignedUrlResponse of(
            final String fileName,
            final String url
    ) {
        return new PreSignedUrlResponse(fileName, url);
    }
}