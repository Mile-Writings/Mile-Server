package com.mile.moim.service.dto;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record BestMoimListResponse(List<BestMoimInfoResponse> moim) {
    public static BestMoimListResponse of(Map<Moim, List<Post>> bestMoimAndPost) {

        List<BestMoimInfoResponse> bestMoims = bestMoimAndPost.entrySet().stream()
                .map(entry -> BestMoimInfoResponse.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new BestMoimListResponse(bestMoims);

    }
}
