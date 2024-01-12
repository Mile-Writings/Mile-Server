package com.mile.moim.service.dto;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;

public record BestMoimInfoResponse(Long moimId, String moimName, List<BestMoimPostResponse> moimPosts) {
    public static BestMoimInfoResponse of(Moim moim, final List<Post> posts) {
        return new BestMoimInfoResponse(
                moim.getId(),
                moim.getName(),
                posts.stream()
                    .map(BestMoimPostResponse::of)
                    .collect(Collectors.toList())
        );
    }
}
