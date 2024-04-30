package com.mile.aws.utils;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum S3BucketDirectory {

    TEST_PREFIX("test/"),
    POST_PREFIX("post/");

    private final String name;

    public String value() {
        return this.name;
    }
}
