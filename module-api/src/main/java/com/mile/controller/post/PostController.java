package com.mile.controller.post;

import com.mile.common.resolver.user.UserId;
import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.post.service.PostService;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.ModifyPostGetResponse;
import com.mile.post.service.dto.PostAuthenticateResponse;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.post.service.dto.PostCuriousResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.post.service.dto.TemporaryPostGetResponse;
import com.mile.common.resolver.post.PostIdPathVariable;
import com.mile.writername.service.dto.WriterNameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController implements PostControllerSwagger {

    private final PostService postService;

    @PostMapping("/{postId}/comment")
    @Override
    public SuccessResponse postComment(
            @PostIdPathVariable final Long postId,
            @Valid @RequestBody final CommentCreateRequest commentCreateRequest,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        postService.createCommentOnPost(
                postId,
                userId,
                commentCreateRequest
        );
        return SuccessResponse.of(SuccessMessage.COMMENT_CREATE_SUCCESS);
    }


    @PostMapping("/{postId}/curious")
    @Override
    public SuccessResponse<PostCuriousResponse> postCurious(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.CURIOUS_CREATE_SUCCESS, postService.createCuriousOnPost(postId, userId));
    }

    @GetMapping("/{postId}/comment")
    @Override
    public ResponseEntity<SuccessResponse<CommentListResponse>> getComments(
            @PostIdPathVariable final Long postId,
            @UserId Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.COMMENT_SEARCH_SUCCESS, postService.getComments(postId, userId)));
    }


    @GetMapping("/{postId}/info/curious")
    @Override
    public ResponseEntity<SuccessResponse<CuriousInfoResponse>> getCuriousInfo(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.CURIOUS_INFO_SEARCH_SUCCESS, postService.getCuriousInfoOfPost(postId, userId)));
    }

    @DeleteMapping("/{postId}/curious")
    @Override
    public SuccessResponse<PostCuriousResponse> deleteCurious(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.CURIOUS_DELETE_SUCCESS, postService.deleteCuriousOnPost(postId, userId));
    }

    @GetMapping("/{postId}/authenticate")
    @Override
    public ResponseEntity<SuccessResponse<PostAuthenticateResponse>> getAuthenticateWrite(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.WRITER_AUTHENTIACTE_SUCCESS, postService.getAuthenticateWriter(postId, userId)));
    }

    @PutMapping("/{postId}")
    @Override
    public ResponseEntity<SuccessResponse> putPost(
            @PostIdPathVariable final Long postId,
            @Valid @RequestBody final PostPutRequest putRequest,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        postService.updatePost(postId, userId, putRequest);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.POST_PUT_SUCCESS));
    }

    @DeleteMapping("/{postId}")
    @Override
    public ResponseEntity<SuccessResponse> deletePost(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        postService.deletePost(postId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.POST_DELETE_SUCCESS));
    }

    @Override
    @GetMapping("/temporary/{postId}")
    public SuccessResponse<TemporaryPostGetResponse> getTemporaryPost(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.TEMPORARY_POST_GET_SUCCESS,
                postService.getTemporaryPost(postId, userId));
    }

    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<SuccessResponse<PostGetResponse>> getPost(
            @PostIdPathVariable final Long postId,
            @PathVariable("postId") final String postUrl
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.POST_GET_SUCCESS, postService.getPost(postId)));
    }


    @Override
    @PostMapping
    public SuccessResponse<WriterNameResponse> createPost(
            @Valid @RequestBody final PostCreateRequest postCreateRequest,
            @UserId final Long userId
    ) {
        return SuccessResponse.of(SuccessMessage.POST_CREATE_SUCCESS, postService.createPost(
                userId,
                postCreateRequest
        ));
    }

    @Override
    @PostMapping("/temporary")
    public SuccessResponse createTemporaryPost(
            @RequestBody @Valid final TemporaryPostCreateRequest temporaryPostCreateRequest,
            @UserId final Long userId
    ) {
        postService.createTemporaryPost(
                userId,
                temporaryPostCreateRequest
        );
        return SuccessResponse.of(SuccessMessage.TEMPORARY_POST_CREATE_SUCCESS);
    }

    @Override
    @DeleteMapping("/temporary/{postId}")
    public ResponseEntity<SuccessResponse> deleteTemporaryPost(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        postService.deleteTemporaryPost(
                userId,
                postId
        );
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.TEMPORARY_POST_DELETE_SUCCESS));
    }

    @Override
    @PutMapping("/temporary/{postId}")
    public ResponseEntity<SuccessResponse<WriterNameResponse>> putTemporaryToFixedPost(
            @PostIdPathVariable final Long postId,
            @RequestBody @Valid final PostPutRequest request,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.POST_CREATE_SUCCESS, postService.putTemporaryToFixedPost(
                userId,
                request,
                postId
        )));
    }

    @Override
    @GetMapping("/modify/{postId}")
    public SuccessResponse<ModifyPostGetResponse> getPostForModifying(
            @PostIdPathVariable final Long postId,
            @UserId final Long userId,
            @PathVariable("postId") final String postUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MODIFY_POST_GET_SUCCESS,
                postService.getPostForModifying(postId, userId));
    }

}
