package com.mile.controller.post;

import com.mile.curious.serivce.dto.CuriousInfoResponse;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.post.service.PostService;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController implements PostControllerSwagger {

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


    @PostMapping("/{postId}/curious")
    @Override
    public SuccessResponse postCurious(
            @PathVariable final Long postId,
            final Principal principal
    ) {
        postService.createCuriousOnPost(postId, Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.CURIOUS_CREATE_SUCCESS);
    }

    @GetMapping("/{postId}/comment")
    @Override
    public SuccessResponse<CommentListResponse> getComments(
            @PathVariable final Long postId,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.COMMENT_SEARCH_SUCCESS, postService.getComments(postId, Long.valueOf(principal.getName())));
    }


    @GetMapping("/{postId}/curiousInfo")
    @Override
    public SuccessResponse<CuriousInfoResponse> getCuriousInfo(
            @PathVariable final Long postId,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.CURIOUS_INFO_SEARCH_SUCCESS, postService.getCuriousInfo(postId, Long.valueOf(principal.getName())));
    }

    @DeleteMapping("/{postId}/curious")
    @Override
    public SuccessResponse deleteCurious(
            @PathVariable final Long postId,
            final Principal principal
    ) {
        postService.deleteCuriousOnPost(postId, Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.CURIOUS_DELETE_SUCCESS);
    }

    @GetMapping("/{postId}/authenticate")
    @Override
    public SuccessResponse getAuthenticateWrite(
            @PathVariable final Long postId,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.WRITER_AUTHENTIACTE_SUCCESS, postService.getAuthenticateWriter(postId, Long.valueOf(principal.getName())));
    }

    @PutMapping("/{postId}")
    @Override
    public SuccessResponse putPost(
            @PathVariable final Long postId,
            @RequestBody final PostPutRequest putRequest,
            final Principal principal
    ) {
        postService.updatePost(postId, Long.valueOf(principal.getName()), putRequest);
        return SuccessResponse.of(SuccessMessage.POST_PUT_SUCCESS);
    }

    @DeleteMapping("/{postId}")
    @Override
    public SuccessResponse deletePost(
            @PathVariable final Long postId,
            final Principal principal
    ) {
        postService.deletePost(postId, Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.POST_DELETE_SUCCESS);
    }

    @Override
    @GetMapping("/{postId}")
    public SuccessResponse<PostGetResponse> getPost(
            @PathVariable final Long postId
    ) {
        return SuccessResponse.of(SuccessMessage.POST_GET_SUCCESS, postService.getPost(postId));
    }
}
