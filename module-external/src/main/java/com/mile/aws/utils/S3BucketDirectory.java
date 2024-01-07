package com.mile.aws.utils;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum S3BucketDirectory {

    TEST_PREFIX("test/");

    private final String name;

    public String value() {
        return this.name;
    }

    public static S3BucketDirectory of(String value) {
        return Arrays.stream(S3BucketDirectory.values())
                .filter(prefix -> value.equals(prefix.name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(INVALID_HEALTH_NOTE));
    }
}
