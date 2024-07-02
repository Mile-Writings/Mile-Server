package com.mile.post.service;

import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.topic.domain.Topic;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PostCreator {
    private final PostRepository postRepository;
    private final SecureUrlUtil secureUrlUtil;
    private static final boolean TEMPORARY_TRUE = true;
    private static final String DEFAULT_IMG_URL = "https://mile-s3.s3.ap-northeast-2.amazonaws.com/test/groupMile.png";
    private static final boolean TEMPORARY_FALSE = false;

    private boolean checkContainPhoto(final String imageUrl) {
        return !imageUrl.equals(DEFAULT_IMG_URL);
    }

    public void createTemporaryPost(
            final WriterName writerName,
            final Topic topic,
            final TemporaryPostCreateRequest temporaryPostCreateRequest
    ) {
        Post post = postRepository.save(Post.create(
                topic, // Topic
                writerName, // WriterName
                temporaryPostCreateRequest.title(),
                temporaryPostCreateRequest.content(),
                temporaryPostCreateRequest.imageUrl(),
                checkContainPhoto(temporaryPostCreateRequest.imageUrl()),
                temporaryPostCreateRequest.anonymous(),
                TEMPORARY_TRUE
        ));
        post.setIdUrl(Base64.getUrlEncoder().encodeToString(post.getId().toString().getBytes()));
    }

    public String create(final PostCreateRequest postCreateRequest, final Topic topic, final WriterName writerName) {
        Post post = createPost(postCreateRequest, topic, writerName);
        postRepository.save(post);
        post.setIdUrl(secureUrlUtil.encodeUrl(post.getId()));
        postRepository.save(post);
        return post.getIdUrl();
    }

    private Post createPost(final PostCreateRequest postCreateRequest, final Topic topic, final WriterName writerName) {
        return Post.create(
                topic,
                writerName, // WriterName
                postCreateRequest.title(),
                postCreateRequest.content(),
                postCreateRequest.imageUrl(),
                checkContainPhoto(postCreateRequest.imageUrl()),
                postCreateRequest.anonymous(),
                TEMPORARY_FALSE
        );
    }
}
