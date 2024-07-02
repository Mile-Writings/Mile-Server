package com.mile.post.service;

import com.mile.post.domain.Post;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.topic.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostUpdator {

    @Transactional
    public void update(
            final Post post,
            final Topic topic,
            final PostPutRequest putRequest
    ) {
        post.updatePost(topic, putRequest);
    }

}
