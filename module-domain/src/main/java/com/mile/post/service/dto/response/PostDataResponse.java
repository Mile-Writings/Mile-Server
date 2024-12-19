package com.mile.post.service.dto.response;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record PostDataResponse(
        Map<String, List<String>> postListMoimMap
) {
    public static PostDataResponse of(Map<Moim, List<Post>> post) {
        return new PostDataResponse(post.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getIdUrl(),
                        entry -> entry.getValue()
                                .stream()
                                .map(Post::getIdUrl)
                                .collect(Collectors.toList())
                )));
    }
}
