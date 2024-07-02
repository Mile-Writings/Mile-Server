package com.mile.comment.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.service.dto.CommentResponse;
import com.mile.commentreply.service.CommentReplyRemover;
import com.mile.commentreply.service.CommentReplyService;
import com.mile.commentreply.service.dto.ReplyCreateRequest;
import com.mile.post.domain.Post;
import com.mile.post.service.PostRetriever;
import com.mile.writername.service.WriterNameRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRetriever postRetriever;
    private final WriterNameRetriever writerNameRetriever;
    private final CommentReplyService commentReplyService;
    private final CommentRetriever commentRetriever;
    private final CommentRemover commentRemover;
    private final CommentReplyRemover commentReplyRemover;


    @Transactional
    public void deleteComment(
            final Long commentId,
            final Long userId
    ) {
        Comment comment = commentRetriever.findById(commentId);
        commentRetriever.authenticateUser(comment, userId);
        commentReplyRemover.deleteRepliesByComment(comment);
        commentRemover.delete(comment);
    }

    public void deleteReply(
            final Long userId,
            final Long replyId
    ) {
        commentReplyService.deleteCommentReply(userId, replyId);
    }

    public List<CommentResponse> getCommentResponse(
            final Long moimId,
            final Long postId,
            final Long userId
    ) {
        postRetriever.authenticateUserWithPostId(postId, userId);
        List<Comment> commentList = commentRetriever.findByPostId(postId);
        Long writerNameId = writerNameRetriever.getWriterNameIdByMoimIdAndUserId(moimId, userId);
        return commentList.stream()
                .map(comment -> CommentResponse.of(
                        comment,
                        writerNameId,
                        commentRetriever.isCommentWriterEqualWriterOfPost(comment, postRetriever.findById(postId)),
                        commentReplyService.findRepliesByComment(comment, writerNameId))).collect(Collectors.toList());
    }

    public String createCommentReply(
            final Long userId,
            final Long commentId,
            final ReplyCreateRequest replyCreateRequest
    ) {
        Comment comment = commentRetriever.findById(commentId);
        return commentReplyService.createCommentReply(
                writerNameRetriever.findWriterNameByMoimIdAndUserId(commentRetriever.getMoimIdFromComment(comment), userId),
                comment,
                replyCreateRequest);
    }

    public int countByPost(
            final Post post
    ) {
        return commentRetriever.countByPost(post) + findCommentReplyByPost(post);
    }

    private int findCommentReplyByPost(
            final Post post
    ) {
        AtomicInteger result = new AtomicInteger();
        commentRetriever.findByPostId(post.getId()).iterator().forEachRemaining(
                c -> result.addAndGet(commentReplyService.findRepliesCountByComment(c))
        );
        return result.intValue();
    }

    public List<Comment> findAllByPosts(
            final List<Post> posts
    ) {
        return commentRetriever.findAllByPosts(posts);
    }

    public void deleteComments(final List<Post> posts) {
        commentRemover.deleteComments(posts);
    }

}
