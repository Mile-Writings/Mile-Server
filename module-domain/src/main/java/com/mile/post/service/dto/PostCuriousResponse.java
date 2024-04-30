package com.mile.post.service.dto;

public record PostCuriousResponse(boolean isCurious) {
    public static PostCuriousResponse of(boolean isCurious) {
        return new PostCuriousResponse(isCurious);
    }
}
