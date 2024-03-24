package com.mile.controller.moim;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.moim.service.dto.BestMoimListResponse;
import com.mile.moim.service.dto.ContentListResponse;
import com.mile.moim.service.dto.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.MoimInfoResponse;
import com.mile.moim.service.dto.MoimTopicResponse;
import com.mile.moim.service.dto.TemporaryPostExistResponse;
import com.mile.moim.service.dto.TopicListResponse;
import com.mile.moim.service.dto.PopularWriterListResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Moim", description = "모임 관련 API")
public interface MoimControllerSwagger {
    @Operation(summary = "에디터 상단 글감 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글감 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 모임의 주제가 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<ContentListResponse> getTopicsFromMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );


    @Operation(summary = "댓글 및 궁금해요 기능 권한 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "사용자의 권한이 확인되었습니다."),
                    @ApiResponse(responseCode = "403", description = "사용자 검증 토큰이 유효하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse getAuthenticationOfMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );


    @Operation(summary = "궁금해요 TOP 2 작가 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "궁금해요 TOP 2 작가가 조회되었습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<PopularWriterListResponse>> getMostCuriousWritersOfMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 최근 글감 ")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글감 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "1. 해당 글모임이 존재하지 않습니다.\n 2.해당 모임의 주제가 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<MoimTopicResponse> getTopicFromMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 뷰 - 글모임 정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글모임 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글모임이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<MoimInfoResponse> getMoimInfo(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );


    @Operation(summary = "글모임 뷰 - 궁금해요 상위 게시글 2개")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "궁금해요 상위 2개의 글이 조회 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글모임이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<MoimCuriousPostListResponse> getMostCuriousPostByMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글감 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글감 리스트 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<TopicListResponse>> getTopicList(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "베스트 활동 모임 및 글 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "베스트 활동 모임과 글 리스트 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<BestMoimListResponse>> getBestMoimAndPostList();


    @Operation(summary = "임시 저장 글 존재 여부 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "임시저장 글 존재 여부 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "1. 해당 글모임이 존재하지 않습니다.\n" +
                            "2. 해당 작가는 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<TemporaryPostExistResponse> getTemporaryPost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 링크 접속 후 모임원 가입")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode =  "201", description = "모임 가입에 완료되었습니다"),
                    @ApiResponse(responseCode = "400" ,description = "1. 소개 글은 최대 110자 이내로 작성해주세요.\n" +
                    "2. 필명이 입력되지 않았습니다.\n" +
                    "3. 이미 존재하는 필명이 있습니다.\n"),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> joinMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @RequestBody final WriterMemberJoinRequest joinRequest,
            @PathVariable("moimId") final String moimUrl
    );
}
