package com.mile.writername.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum MoimRole {

    WRITER("WRITER"),
    OWNER("OWNER");

    private final String role;

}
