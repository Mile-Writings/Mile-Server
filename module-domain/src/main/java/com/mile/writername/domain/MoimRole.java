package com.mile.writername.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MoimRole {

    WRITER("WRITER"),
    OWNER("OWNER");

    private final String role;

}
