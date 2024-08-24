package com.mile.post.service;

import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.request.PostPutRequest;
import com.mile.topic.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostUpdator {


    private final PostRetriever postRetriever;
    private final PostRepository postRepository;

    public void update(
            final Post post,
            final Topic topic,
            final PostPutRequest putRequest
    ) {
        post.updatePost(topic, putRequest);
        postRepository.save(post);
    }

    public void updateTemporaryPost(
            final Post post,
            final Topic topic,
            final PostPutRequest putRequest
    ) {
        post.setTemporary(false);
        post.updateCratedAt(LocalDateTime.now());
        update(post, topic, putRequest);
    }

}
