package com.mile.post.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.topic.domain.Topic;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostGetService {

    private final PostRepository postRepository;
    private final SecureUrlUtil secureUrlUtil;
    private static final int POST_BY_TOPIC_PER_PAGE_SIZE = 6;

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
        Optional<Post> post = postRepository.findByMoimAndWriterNameWhereIsTemporary(moim, writerName);
        if (post.isEmpty()) {
            return secureUrlUtil.encodeUrl(0L);
        }
        return post.get().getIdUrl();
    }

    private boolean isPostListEmpty(
            final List<Post> postList
    ) {
        return postList.isEmpty();
    }

    public Slice<Post> findByTopicAndLastPostId(
            final Topic topic,
            final Long lastPostId
    ) {
        PageRequest pageRequest = PageRequest.of(0, POST_BY_TOPIC_PER_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Slice<Post> posts = postRepository.findByTopicAndLastPostId(topic, pageRequest, lastPostId);
        return posts;
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

    public List<Post> findAllByTopics(
            final List<Topic> topics
    ) {
        return topics.stream()
                .flatMap(topic -> postRepository.findByTopic(topic).stream())
                .collect(Collectors.toList());
    }

}
