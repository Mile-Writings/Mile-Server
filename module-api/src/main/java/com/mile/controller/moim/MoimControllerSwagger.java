package com.mile.controller.moim;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.moim.serivce.dto.CategoryListResponse;
import com.mile.moim.serivce.dto.ContentListResponse;
import com.mile.moim.serivce.dto.MoimInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

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
            @PathVariable final Long moimId,
            final Principal principal
    );


    @Operation(summary = "댓글 및 궁금해요 기능 권한 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "사용자의 권한이 확인되었습니다."),
                    @ApiResponse(responseCode = "403", description = "사용자 검증 토큰이 유효하지 안습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse getAuthenticationOfMoim(
            final Long moimId,
            final Principal principal
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
            final Long moimId
    );

    @Operation(summary = "카테고리 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 리스트 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public SuccessResponse<CategoryListResponse> getCategoryList(
            final Long moimId
    );
}
