package com.mile.controller.comment;


import com.mile.comment.service.CommentService;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
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

    @DeleteMapping("/{commentId}")
    public SuccessResponse deleteComment(
            @PathVariable final Long commentId,
            final Principal principal
    ) {
        commentService.deleteComment(commentId, Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.COMMENT_DELETE_SUCCESS);
    }
}
