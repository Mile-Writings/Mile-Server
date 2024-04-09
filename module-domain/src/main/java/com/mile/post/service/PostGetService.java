package com.mile.post.service;

import com.mile.config.BaseTimeEntity;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.topic.domain.Topic;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostGetService {

    private final PostRepository postRepository;
    private final SecureUrlUtil secureUrlUtil;

    public Post findById(
            final Long postId
    ) {
        return postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
        );

    }


    public String getTemporaryPostExist(
            final Moim moim,
            final WriterName writerName
    ) {
        List<Post> postList = postRepository.findByMoimAndWriterNameWhereIsTemporary(moim, writerName);
        if (isPostListEmpty(postList)) {
            return secureUrlUtil.encodeUrl(0L);
        }
        postList.sort(Comparator.comparing(Post::getCreatedAt).reversed());
        return postList.get(0).getIdUrl();
    }

    private boolean isPostListEmpty(
            final List<Post> postList
    ) {
        return postList.isEmpty();
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

    public List<Post> findAllByTopic(
            final Topic topic
    ) {
        return postRepository.findByTopic(topic);
    }

    public int findPostCountByWriterNameId(
            final Long writerNameId
    ) {
        return postRepository.countByWriterNameId(writerNameId);
    }

}
