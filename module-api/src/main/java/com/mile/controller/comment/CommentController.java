package com.mile.controller.comment;


import com.mile.authentication.PrincipalHandler;
import com.mile.comment.service.CommentService;
import com.mile.commentreply.service.dto.ReplyCreateRequest;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.resolver.comment.CommentIdPathVariable;
import com.mile.resolver.reply.ReplyIdPathVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController implements CommentControllerSwagger {

    private final CommentService commentService;
    private final PrincipalHandler principalHandler;

    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(
            @CommentIdPathVariable final Long commentId,
            @PathVariable("commentId") final String commentUrl
    ) {
        commentService.deleteComment(commentId, principalHandler.getUserIdFromPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.COMMENT_DELETE_SUCCESS));
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> createCommentReply(
            @CommentIdPathVariable final Long commentId,
            @RequestBody final ReplyCreateRequest createRequest,
            @PathVariable("commentId") final String commentUrl
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).header("Location",
                commentService.createCommentReply(
                        principalHandler.getUserIdFromPrincipal(),
                        commentId, createRequest
                )).body(SuccessResponse.of(SuccessMessage.REPLY_CREATE_SUCCESS));
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<SuccessResponse> deleteCommentReply(
            @ReplyIdPathVariable final Long replyId,
            @PathVariable("replyId") final String replyUrl
    ) {
        commentService.deleteReply(principalHandler.getUserIdFromPrincipal(), replyId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.REPLY_DELETE_SUCCESS));
    }
}
