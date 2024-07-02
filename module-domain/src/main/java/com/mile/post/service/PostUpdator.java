package com.mile.post.service;

import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.topic.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            final Long postId,
            final Topic topic,
            final PostPutRequest putRequest
    ) {
        Post post = postRetriever.findById(postId);
        post.setTemporary(false);
        post.updateCratedAt(LocalDateTime.now());
        update(post, topic, putRequest);
    }

}
