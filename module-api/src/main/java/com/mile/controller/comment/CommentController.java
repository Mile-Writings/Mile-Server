package com.mile.controller.comment;


import com.mile.comment.service.CommentService;
import com.mile.commentreply.service.dto.request.ReplyCreateRequest;
import com.mile.common.resolver.comment.CommentIdPathVariable;
import com.mile.common.resolver.reply.ReplyIdPathVariable;
import com.mile.common.resolver.user.UserId;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController implements CommentControllerSwagger {

    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(
            @CommentIdPathVariable final Long commentId,
            @UserId final Long userId,
            @PathVariable("commentId") final String commentUrl
    ) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.COMMENT_DELETE_SUCCESS));
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> createCommentReply(
            @CommentIdPathVariable final Long commentId,
            @UserId final Long userId,
            @Valid @RequestBody final ReplyCreateRequest createRequest,
            @PathVariable("commentId") final String commentUrl
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).header("Location",
                commentService.createCommentReply(
                        userId,
                        commentId,
                        createRequest
                )).body(SuccessResponse.of(SuccessMessage.REPLY_CREATE_SUCCESS));
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<SuccessResponse> deleteCommentReply(
            @ReplyIdPathVariable final Long replyId,
            @UserId final Long userId,
            @PathVariable("replyId") final String replyUrl
    ) {
        commentService.deleteReply(userId, replyId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.REPLY_DELETE_SUCCESS));
    }
}
