package com.mile.external.client;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("KAKAO"),
    ;
    private String type;
}
