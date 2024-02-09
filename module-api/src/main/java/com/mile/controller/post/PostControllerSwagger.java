package com.mile.controller.post;

import com.mile.curious.service.dto.CuriousInfoResponse;
import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.post.service.dto.CommentListResponse;
import com.mile.post.service.dto.ModifyPostGetResponse;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.post.service.dto.PostCuriousResponse;
import com.mile.post.service.dto.PostGetResponse;
import com.mile.post.service.dto.PostPutRequest;
import com.mile.post.service.dto.TemporaryPostCreateRequest;
import com.mile.post.service.dto.TemporaryPostGetResponse;
import com.mile.writername.service.dto.WriterNameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Post", description = "게시글 관련 API - 댓글 등록/ 조회 및 궁금해요 등록/삭제 포함")
public interface PostControllerSwagger {

    @Operation(summary = "댓글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록이 완료되었습니다."),
                    @ApiResponse(responseCode = "400",
                            description = "1. 댓글 최대 입력 길이(500자)를 초과하였습니다.\n" +
                                    "2.댓글에 내용이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse postComment(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @Valid @RequestBody final CommentCreateRequest commentCreateRequest,
            @PathVariable("postId") final String postUrl
    );


    @Operation(summary = "궁금해요 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "궁금해요 생성이 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<PostCuriousResponse> postCurious(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );

    @Operation(summary = "댓글 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 글에 대한 댓글이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<CommentListResponse> getComments(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );


    @Operation(summary = "궁금해요 개수 및 궁금해요 여부 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "궁금해요 개수 및 궁금해요 여부 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<CuriousInfoResponse>> getCuriousInfo(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );


    @Operation(summary = "궁금해요 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "궁금해요 삭제가 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<PostCuriousResponse>> deleteCurious(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );


    @Operation(summary = "게시글 삭제/수정 권한 확인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 권한이 확인되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    SuccessResponse getAuthenticateWrite(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );


    @Operation(summary = "글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글 수정이 완료되었습니다."),
                    @ApiResponse(
                            responseCode = "400", description = "1. 글감 id가 입력되지 않았습니다.\n" +
                            "2. 제목을 입력해주세요.\n" +
                            "3. 제목 최대 글자를 초과했습니다\n" +
                            "4. 내용을 입력해주세요.\n" +
                            "5. 내용 최대 글자를 초과했습니다.\n" +
                            "6. 이미지 url을 입력해주세요.\n" +
                            "7. 익명 여부를 입력해주세요."
                    ),
                    @ApiResponse(responseCode = "404", description = "1. 해당 글은 존재하지 않습니다.\n" + "2. 해당 글감이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 글 수정/삭제 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    ResponseEntity<SuccessResponse> putPost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @RequestBody final PostPutRequest putRequest,
            @PathVariable("postId") final String postUrl
    );

    @Operation(summary = "글 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글 삭제가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 글 수정/삭제 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> deletePost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );

    @Operation(summary = "임시저장글 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "임시저장글 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<TemporaryPostGetResponse> getTemporaryPost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );

    @Operation(summary = "글 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<PostGetResponse> getPost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );

    @Operation(summary = "글 임시저장")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글 임시저장이 완료되었습니다."),
                    @ApiResponse(responseCode = "400",
                            description = "1. 제목 최대 글자(29)를 초과했습니다.\n" +
                                    "2. 내용 최대 글자(2500)를 초과했습니다.\n" +
                                    "3. 모임 ID가 없습니다.\n" +
                                    "4. 글감 ID가 없습니다.\n",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse createTemporaryPost(
            @Valid @RequestBody final TemporaryPostCreateRequest temporaryPostCreateRequest
    );


    @Operation(summary = "글 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글 생성이 완료되었습니다."),
                    @ApiResponse(responseCode = "400",
                            description = "1. 제목 최대 글자(29)를 초과했습니다.\n" +
                                    "2. 내용 최대 글자(2500)를 초과했습니다.\n" +
                                    "3. 제목의 내용이 없습니다.\n" +
                                    "4. 내용의 내용이 없습니다.\n" +
                                    "5. 모임 ID가 없습니다.\n" +
                                    "6. 글감 ID가 없습니다.\n",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse createPost(
            @Valid @RequestBody final PostCreateRequest postCreateRequest
    );


    @Operation(summary = "임시 저장된 글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글 작성이 완료되었습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    SuccessResponse<WriterNameResponse> putTemporaryToFixedPost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @RequestBody final PostPutRequest request,
            @PathVariable("postId") final String postUrl
    );

    @Operation(summary = "글 수정 시 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글 수정 시 글 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<ModifyPostGetResponse> getPostForModifying(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long postId,
            @PathVariable("postId") final String postUrl
    );
}
