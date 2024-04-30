package com.mile.post.service;

import com.mile.config.BaseTimeEntity;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.topic.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostGetService {

    private final PostRepository postRepository;


    public Post findById(
            final Long postId
    ) {
        return postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
        );
    }

    public List<Post> findByTopic(
            final Topic topic
    ) {
        List<Post> postList = postRepository.findByTopic(topic);
        postList.removeIf(Post::isTemporary);
        return postList.stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed()).collect(Collectors.toList());
    }


    public List<Post> getLatestPostsByMoim(Moim moim) {
        return postRepository.findLatest4NonTemporaryPostsByMoim(moim);
    }
}
