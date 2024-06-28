package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.repository.CommentRepository;
import com.mile.comment.service.dto.CommentResponse;
import com.mile.commentreply.service.CommentReplyService;
import com.mile.commentreply.service.dto.ReplyCreateRequest;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.post.domain.Post;
import com.mile.post.service.PostAuthenticateService;
import com.mile.post.service.PostGetService;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostAuthenticateService postAuthenticateService;
    private final CommentRepository commentRepository;
    private final PostGetService postGetService;
    private final SecureUrlUtil secureUrlUtil;
    private final WriterNameService writerNameService;
    private final CommentReplyService commentReplyService;

    @Transactional
    public void deleteComment(
            final Long commentId,
            final Long userId
    ) {
        Comment comment = findById(commentId);
        authenticateUser(comment, userId);
        commentReplyService.deleteRepliesByComment(comment);
        delete(comment);
    }

    private void delete(
            final Comment comment
    ) {
        commentRepository.delete(comment);
    }

    private Comment findById(
            final Long commentId
    ) {
        return commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND)
                );
    }

    private void authenticateUser(
            final Comment comment,
            final Long userId
    ) {
        if (!commentRepository.findUserIdByComment(comment).equals(userId)) {
            throw new ForbiddenException(ErrorMessage.COMMENT_ACCESS_ERROR);
        }
    }

    @Transactional
    public void createComment(
            final Post post,
            final WriterName writerName,
            final CommentCreateRequest commentCreateRequest
    ) {
        Comment comment = create(post, writerName, commentCreateRequest);
        comment.setIdUrl(secureUrlUtil.encodeUrl(comment.getId()));
    }


    public void deleteReply(
            final Long userId,
            final Long replyId
    ) {
        commentReplyService.deleteCommentReply(userId, replyId);
    }

    private Comment create(
            final Post post,
            final WriterName writerName,
            final CommentCreateRequest commentCreateRequest
    ) {
        return commentRepository.saveAndFlush(Comment.create(post, writerName, commentCreateRequest));

    }

    public List<CommentResponse> getCommentResponse(
            final Long moimId,
            final Long postId,
            final Long userId
    ) {
        postAuthenticateService.authenticateUserWithPostId(postId, userId);
        List<Comment> commentList = findByPostId(postId);
        Long writerNameId = writerNameService.getWriterNameIdByMoimIdAndUserId(moimId, userId);
        return commentList.stream()
                .map(comment -> CommentResponse.of(
                        comment,
                        writerNameId,
                        isCommentWriterEqualWriterOfPost(comment, postId),
                        commentReplyService.findRepliesByComment(comment, writerNameId))).collect(Collectors.toList());
    }


    public String createCommentReply(
            final Long userId,
            final Long commentId,
            final ReplyCreateRequest replyCreateRequest
    ) {
        Comment comment = findById(commentId);
        return commentReplyService.createCommentReply(
                writerNameService.findWriterNameByMoimIdAndUserId(getMoimIdFromComment(comment), userId),
                comment,
                replyCreateRequest);
    }

    private Long getMoimIdFromComment(final Comment comment) {
        return comment.getPost().getTopic().getMoim().getId();
    }

    private boolean isCommentWriterEqualWriterOfPost(
            final Comment comment,
            final Long postId
    ) {
        Post post = postGetService.findById(postId);
        return post.getWriterName().equals(comment.getWriterName());
    }

    public List<Comment> findByPostId(
            final Long postId
    ) {
        return commentRepository.findByPostId(postId);
    }


    private int findCommentReplyByPost(
            final Post post
    ) {
        AtomicInteger result = new AtomicInteger();
        findByPostId(post.getId()).iterator().forEachRemaining(
                c -> result.addAndGet(commentReplyService.findRepliesCountByComment(c))
        );
        return result.intValue();
    }


    public void deleteAllByPost(
            final Post post
    ) {
        commentRepository.findByPostId(post.getId()).forEach(commentReplyService::deleteRepliesByComment);
        commentRepository.deleteAllByPost(post);
    }

    public void deleteAllCommentByWriterNameId(
            final Long writerNameId
    ) {
        commentRepository.deleteAllByWriterNameId(writerNameId);
    }

    public int countByPost(
            final Post post
    ) {
        return commentRepository.countByPost(post) + findCommentReplyByPost(post);
    }

    public List<Comment> findAllByPosts(
            final List<Post> posts
    ) {
        return posts.stream()
                .flatMap(post -> findByPostId(post.getId()).stream())
                .collect(Collectors.toList());
    }

    public void deleteComments(final List<Post> posts) {
        posts.forEach(this::deleteAllByPost);
    }
}
