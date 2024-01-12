package com.mile.moim.service.dto;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record BestMoimListResponse(List<BestMoimInfoResponse> moim) {
    public static BestMoimListResponse of(Map<Moim, List<Post>> BestMoimAndPost) {
        List<BestMoimInfoResponse> bestMoims = new ArrayList<>();
        for (Moim moim : BestMoimAndPost.keySet()) {
            bestMoims.add(BestMoimInfoResponse.of(moim, BestMoimAndPost.get(moim)));
        }
        return new BestMoimListResponse(bestMoims);

    }
}
