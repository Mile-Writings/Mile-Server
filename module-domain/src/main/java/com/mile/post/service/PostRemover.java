package com.mile.post.service;

import com.mile.aws.utils.S3Service;
import com.mile.comment.service.CommentRemover;
import com.mile.curious.service.CuriousRemover;
import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.topic.domain.Topic;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostRemover {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final CommentRemover commentRemover;
    private final CuriousRemover curiousRemover;


    public void deleteTemporaryPosts(
            final Moim moim,
            final WriterName writerName
    ) {
        postRepository.findByMoimAndWriterNameWhereIsTemporary(moim, writerName.getId()).ifPresent(
                this::delete
        );
    }

    @Transactional
    public void deleteTemporaryPost(
            final Post post
    ) {
        postRepository.delete(post);

    }

    public void delete(
            final Post post
    ) {
        deleteRelatedData(post);
        postRepository.delete(post);
    }


    private void deleteRelatedData(
            final Post post
    ) {
        if (post.isContainPhoto()) {
            deleteS3File(post.getImageUrl());
        }

        WriterName writerName = post.getWriterName();
        writerName.decreaseTotalCuriousCountByPostDelete(post.getCuriousCount());

        curiousRemover.deleteAllByPost(post);
        commentRemover.deleteAllByPost(post);
    }

    private void deleteS3File(
            final String key
    ) {
        s3Service.deleteImage(key);
    }


    public void deleteAllPostByWriterNameId(final WriterName writerName) {
        postRepository.deleteByWriterName(writerName);
    }

    public void deletePostsByTopic(final List<Topic> topics) {
        topics.forEach(postRepository::deleteByTopic);
    }
}
