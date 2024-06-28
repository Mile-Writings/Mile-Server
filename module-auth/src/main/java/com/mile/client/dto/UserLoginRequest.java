package com.mile.client.dto;

import com.mile.client.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(
        @NotBlank
        @Schema(description = "리다이텍트 uri 값", example = "http://localhost:5173/redirect-kakao or https://milewriting.com/redirect-kakao or http://localhost:8080/login/oauth2/code/google")
        String redirectUri,
        @NotNull(message = "소셜 로그인 종류가 입력되지 않았습니다.")
        @Schema(description = "소셜로그인 타입", example = "KAKAO or GOOGLE")
        SocialType socialType
) {
}
