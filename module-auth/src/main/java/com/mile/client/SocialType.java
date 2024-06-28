package com.mile.client;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),
    ;
    private final String socialType;
}
