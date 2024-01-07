package com.mile.controller.post;

import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.post.service.PostService;
import com.mile.post.service.dto.CommentCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController implements PostControllerSwagger{

    private final PostService postService;

    @PostMapping("/{postId}/comment")
    @Override
    public SuccessResponse postComment(
            @PathVariable final Long postId,
            @Valid @RequestBody final CommentCreateRequest commentCreateRequest,
            final Principal principal
    ) {
        postService.createCommentOnPost(
                postId,
                Long.valueOf(principal.getName()),
                commentCreateRequest
        );
        return SuccessResponse.of(SuccessMessage.COMMENT_CREATE_SUCCESS);
    }
}
