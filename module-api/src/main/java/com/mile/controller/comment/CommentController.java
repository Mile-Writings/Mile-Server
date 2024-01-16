package com.mile.controller.comment;


import com.mile.authentication.PrincipalHandler;
import com.mile.comment.service.CommentService;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.resolver.comment.CommentIdPathVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController implements CommentControllerSwagger{

    private final CommentService commentService;
    private final PrincipalHandler principalHandler;

    @DeleteMapping("/{commentId}")
    public SuccessResponse deleteComment(
            @CommentIdPathVariable final Long commentId,
            @PathVariable("commentId") final String commentUrl
    ) {
        commentService.deleteComment(commentId, principalHandler.getUserIdFromPrincipal());
        return SuccessResponse.of(SuccessMessage.COMMENT_DELETE_SUCCESS);
    }
}
