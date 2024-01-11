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
    private static final int SUBSTRING_START = 0;
    private static final int SUBSTRING_FINAL = 72;

    private void isMostCuriousPostEmpty(final List<Post> postList) {
        if (postList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.MOIM_POST_NOT_FOUND);
        }
    }

    public MoimCuriousPostListResponse getMostCuriousPostByMoim(final Moim moim) {
        List<Post> postList = postRepository.findTop2ByMoimOrderByCuriousCountDesc(moim);
        isMostCuriousPostEmpty(postList);
        return MoimCuriousPostListResponse.of(postList
                .stream()
                .map(p ->
                        MoimMostCuriousPostResponse.of(p.getImageUrl(), p.getTopic().getContent(), p.getTitle(), getSubStringOfContent(p.getContent()))
                ).collect(Collectors.toList()));
    }

    private String getSubStringOfContent(final String content) {
        return content.substring(SUBSTRING_START, SUBSTRING_FINAL);
    }

}
