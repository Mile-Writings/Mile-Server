package com.mile.controller.moim;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.moim.service.dto.BestMoimListResponse;
import com.mile.moim.service.dto.ContentListResponse;
import com.mile.moim.service.dto.InvitationCodeGetResponse;
import com.mile.moim.service.dto.MoimCreateRequest;
import com.mile.moim.service.dto.MoimCreateResponse;
import com.mile.moim.service.dto.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.MoimInfoModifyRequest;
import com.mile.moim.service.dto.MoimInfoOwnerResponse;
import com.mile.moim.service.dto.MoimInfoResponse;
import com.mile.moim.service.dto.MoimInvitationInfoResponse;
import com.mile.moim.service.dto.MoimNameConflictCheckResponse;
import com.mile.moim.service.dto.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.MoimTopicResponse;
import com.mile.moim.service.dto.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.PopularWriterListResponse;
import com.mile.moim.service.dto.TemporaryPostExistResponse;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.moim.service.dto.TopicListResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.moim.service.dto.WriterNameConflictCheckResponse;
import com.mile.resolver.moim.MoimIdPathVariable;
import com.mile.writername.service.dto.WriterNameShortResponse;
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
import org.springframework.web.bind.annotation.RequestParam;

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

    @Operation(summary = "초대 링크에서 모임 정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 글모임이 존재하지 않습니다.\n",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<MoimInvitationInfoResponse>> getInvitationInfo(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "필명 중복 확인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 중복 여부가 조회되었습니다."),
                    @ApiResponse(responseCode = "400", description = "사용 불가능한 필명입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "1. 해당 모임은 존재하지 않습니다.\n",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<WriterNameConflictCheckResponse>> checkConflictOfWriterName(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            final String writerName,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 링크 접속 후 모임원 가입")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "모임 가입에 완료되었습니다"),
                    @ApiResponse(responseCode = "400", description = "1. 소개 글은 최대 110자 이내로 작성해주세요.\n" +
                            "2. 필명이 입력되지 않았습니다.\n" +
                            "3. 필명은 최대 8자 이내로 작성해주세요.\n"),
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

    @Operation(summary = "관리자 페이지 글감 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글감 리스트 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다."),
                    @ApiResponse(responseCode = "403", description = "사용자는 해당 모임의 모임장이 아닙니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<MoimTopicInfoListResponse>> getMoimTopicList(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            final int page,
            @PathVariable("moimId") final String moimUrl
    );


    @Operation(summary = "관리자 페이지 모임 정보 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "모임 정보 수정이 완료되었습니다."),
                    @ApiResponse(responseCode = "400", description = "1. 소개 글은 최대 100자 이내로 작성해주세요.\n" +
                            "2. 글모임 이름은 최대 10 글자 이내로 작성해주세요.\n"),
                    @ApiResponse(responseCode = "401", description = "로그인 후 진행해주세요."),
                    @ApiResponse(responseCode = "403", description = "사용자는 해당 모임의 모임장이 아닙니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    ResponseEntity<SuccessResponse> modifyMoimInformation(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @RequestBody final MoimInfoModifyRequest request,
            @PathVariable("moimId") final String moimUrl
    );


    @Operation(summary = "글모임 이름 중복확인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글모임 이름 중복 확인이 완료되었습니다."),
                    @ApiResponse(responseCode = "400", description = "사용 불가능한 모임명입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<MoimNameConflictCheckResponse>> validateMoimName(
            @RequestParam final String moimName
    );

    @Operation(summary = "초대링크 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "초대링크 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다."),
                    @ApiResponse(responseCode = "403", description = "사용자는 해당 모임의 모임장이 아닙니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<InvitationCodeGetResponse>> getInvitationCode(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "글감 리스트 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "400", description = "1. 글모임명은 최대 10글자 이내로 작성해주세요.\n" +
                            "2. 필명은 최대 8글자 이내로 작성해주세요.\n" +
                            "3. 글모임장 소개글은 최대 100자 이내로 작성해주세요." +
                            "4. 글감 소개글은 최대 90자 이내로 작성해주세요."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<MoimCreateResponse>> createMoim(
            @RequestBody final MoimCreateRequest creatRequest
    );


    @Operation(summary = "관리자 페이지 글모임 글감 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "글감 생성이 완료되었습니다."),
                    @ApiResponse(responseCode = "400", description = "1. 글감은 최대 15자 이내로 작성해주세요.\n"
                            + "2. 글감 제목이 비어있습니다.\n" + "3. 글감 태그는 최대 5자 이내로 작성해주세요.\n"
                            + "4. 글감 태그가 비어있습니다.\n" + "5. 글감 설명은 최대 90자 이내로 작성해주세요."
                    ),
                    @ApiResponse(responseCode = "401", description = "로그인 후 이용해주세요.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "사용자는 해당 모임의 모임장이 아닙니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> createTopicOfMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @RequestBody final TopicCreateRequest createRequest,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "관리자 페이지 모임 정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "관리자 페이지의 모임 정보가 조회되었습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "로그인 후 이용해주세요.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "사용자는 해당 모임의 모임장이 아닙니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<MoimInfoOwnerResponse>> getMoimInfoForOwner(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "관리자 페이지 멤버 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "멤버 리스트 조회가 조회되었습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "로그인 후 이용해주세요.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "사용자는 해당 모임의 모임장이 아닙니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<MoimWriterNameListGetResponse>> getWriterNameListOfMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @RequestParam final int page,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 뷰 - 내 필명 GET")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "필명 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 모임에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<WriterNameShortResponse>> getWriterNameOfUser(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 공개여부 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글모임 공개여부 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse getPublicStatusOfMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );

    @Operation(summary = "글모임 삭제 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글모임 삭제가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> deleteMoim(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    );
}
