package com.mile.post.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.MoimMostCuriousPostResponse;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCuriousService {

    private final PostRepository postRepository;

    private List<Post> getPostHaveCuriousCount(
            final List<Post> postList
    ) {
        postList.removeIf(post -> post.getCuriousCount() <= 0);
        return postList;
    }

    public MoimCuriousPostListResponse getMostCuriousPostByMoim(final Moim moim) {
        List<Post> postList = getPostHaveCuriousCount(postRepository.findTop2ByMoimOrderByCuriousCountDesc(moim));
        return MoimCuriousPostListResponse.of(postList
                .stream()
                .map(p ->
                        MoimMostCuriousPostResponse.of(p.getIdUrl(), p.getImageUrl(), p.getTopic().getContent(), p.getTitle(), p.getContent(), p.isContainPhoto())
                ).collect(Collectors.toList()));
    }

}
