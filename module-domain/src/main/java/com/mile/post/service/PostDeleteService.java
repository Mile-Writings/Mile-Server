package com.mile.post.service;

import com.mile.aws.utils.S3Service;
import com.mile.comment.service.CommentService;
import com.mile.curious.service.CuriousService;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.MoimMostCuriousPostResponse;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostDeleteService {

    private final PostRepository postRepository;
    private final CuriousService curiousService;
    private final CommentService commentService;
    private final S3Service s3Service;

    private List<Post> getPostHaveCuriousCount(
            final List<Post> postList
    ) {
        postList.removeIf(post -> post.getCuriousCount() <= 0);
        return postList;
    }

    public void deleteTemporaryPosts(
            final Moim moim,
            final WriterName writerName
    ) {
        postRepository.findByMoimAndWriterNameWhereIsTemporary(moim, writerName).ifPresent(
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

    public void deletePosts(
            final List<Post> posts
    ) {
        posts.forEach(post -> delete(post));
    }

    private void deleteRelatedData(
            final Post post
    ) {
        if (post.isContainPhoto()) {
            deleteS3File(post.getImageUrl());
        }

        WriterName writerName = post.getWriterName();
        writerName.decreaseTotalCuriousCountByPostDelete(post.getCuriousCount());

        curiousService.deleteAllByPost(post);
        commentService.deleteAllByPost(post);
    }

    private void deleteS3File(
            final String key
    ) {
        s3Service.deleteImage(key);
    }

    public MoimCuriousPostListResponse getMostCuriousPostByMoim(final Moim moim) {
        List<Post> postList = getPostHaveCuriousCount(postRepository.findTop2ByMoimOrderByCuriousCountDesc(moim));
        return MoimCuriousPostListResponse.of(postList
                .stream()
                .map(p ->
                        MoimMostCuriousPostResponse.of(p.getIdUrl(), p.getImageUrl(), p.getTopic().getContent(), p.getTitle(), p.getContent(), p.isContainPhoto())
                ).collect(Collectors.toList()));
    }


    public void deleteAllPostByWriterNameId(final Long writerNameId) {
        List<Post> posts = postRepository.findByWriterNameId(writerNameId);
        posts.forEach(this::delete);
    }
}
