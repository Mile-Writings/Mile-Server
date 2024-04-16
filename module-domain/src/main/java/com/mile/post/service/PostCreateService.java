package com.mile.post.service;

import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.topic.service.TopicService;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PostCreateService {
    private final PostRepository postRepository;
    private final TopicService topicService;
    private final SecureUrlUtil secureUrlUtil;
    private static final boolean TEMPORARY_TRUE = true;
    private static final String DEFAULT_IMG_URL = "https://mile-s3.s3.ap-northeast-2.amazonaws.com/post/KakaoTalk_Photo_2024-01-14-15-52-49.png";


    private boolean checkContainPhoto(final String imageUrl) {
        return !imageUrl.equals(DEFAULT_IMG_URL);
    }

    public void createTemporaryPost(
            final WriterName writerName,
            final TemporaryPostCreateRequest temporaryPostCreateRequest
    ) {
        Post post = postRepository.save(Post.create(
                topicService.findById(secureUrlUtil.decodeUrl(temporaryPostCreateRequest.topicId())), // Topic
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
}
