package com.mile.controller.post;

import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.post.service.PostService;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.post.service.dto.PostCuriousResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.post.service.dto.TemporaryPostGetResponse;
import com.mile.resolver.post.PostIdPathVariable;
import com.mile.writerName.service.dto.WriterNameResponse;
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
            @PostIdPathVariable final Long postId,
            @Valid @RequestBody final CommentCreateRequest commentCreateRequest,
            final Principal principal,
            @PathVariable("postId") final String postUrl
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
    public SuccessResponse<PostCuriousResponse> postCurious(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.CURIOUS_CREATE_SUCCESS, postService.createCuriousOnPost(postId, Long.valueOf(principal.getName())));
    }

    @GetMapping("/{postId}/comment")
    @Override
    public SuccessResponse<CommentListResponse> getComments(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.COMMENT_SEARCH_SUCCESS, postService.getComments(postId, Long.valueOf(principal.getName())));
    }


    @GetMapping("/{postId}/curiousInfo")
    @Override
    public SuccessResponse<CuriousInfoResponse> getCuriousInfo(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.CURIOUS_INFO_SEARCH_SUCCESS, postService.getCuriousInfo(postId, Long.valueOf(principal.getName())));
    }

    @DeleteMapping("/{postId}/curious")
    @Override
    public SuccessResponse<PostCuriousResponse> deleteCurious(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.CURIOUS_DELETE_SUCCESS, postService.deleteCuriousOnPost(postId, Long.valueOf(principal.getName())));
    }

    @GetMapping("/{postId}/authenticate")
    @Override
    public SuccessResponse getAuthenticateWrite(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.WRITER_AUTHENTIACTE_SUCCESS, postService.getAuthenticateWriter(postId, Long.valueOf(principal.getName())));
    }

    @PutMapping("/{postId}")
    @Override
    public SuccessResponse putPost(
            @PostIdPathVariable final Long postId,
            @Valid @RequestBody final PostPutRequest putRequest,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        postService.updatePost(postId, Long.valueOf(principal.getName()), putRequest);
        return SuccessResponse.of(SuccessMessage.POST_PUT_SUCCESS);
    }

    @DeleteMapping("/{postId}")
    @Override
    public SuccessResponse deletePost(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        postService.deletePost(postId, Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.POST_DELETE_SUCCESS);
    }

    @Override
    @GetMapping("/temporary/{postId}")
    public SuccessResponse<TemporaryPostGetResponse> getTemporaryPost(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.TEMPORARY_POST_GET_SUCCESS,
                postService.getTemporaryPost(postId, Long.valueOf(principal.getName())));
    }

    @Override
    @GetMapping("/{postId}")
    public SuccessResponse<PostGetResponse> getPost(
            @PostIdPathVariable final Long postId,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.POST_GET_SUCCESS, postService.getPost(postId));
    }


    @Override
    @PostMapping
    public SuccessResponse<WriterNameResponse> createPost(
            @Valid @RequestBody final PostCreateRequest postCreateRequest,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.POST_CREATE_SUCCESS, postService.createPost(
                Long.valueOf(principal.getName()),
                postCreateRequest
        ));
    }

    @PostMapping("/temporary")
    public SuccessResponse createTemporaryPost(
            @RequestBody final TemporaryPostCreateRequest temporaryPostCreateRequest,
            final Principal principal
    ) {
        postService.createTemporaryPost(
                Long.valueOf(principal.getName()),
                temporaryPostCreateRequest
        );
        return SuccessResponse.of(SuccessMessage.TEMPORARY_POST_CREATE_SUCCESS);
    }

    @PutMapping("/temporary/{postId}")
    public SuccessResponse<WriterNameResponse> putFixedPost(
            @PostIdPathVariable final Long postId,
            final Principal principal,
            @RequestBody final PostPutRequest request,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.POST_CREATE_SUCCESS, postService.putFixedPost(
                Long.valueOf(principal.getName()),
                request,
                postId
        ));
    }
}
