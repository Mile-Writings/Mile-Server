package com.mile.post.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.topic.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostGetService {

    private final PostRepository postRepository;


    public List<Post> findByTopic(
            final Topic topic
    ) {
        List<Post> postList = postRepository.findByTopic(topic);
        isPostListEmpty(postList);
        return postList;
    }

    private void isPostListEmpty(
            List<Post> postList
    ) {
        if(postList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.MOIM_TOPIC_NOT_FOUND);
        }
    }
}
