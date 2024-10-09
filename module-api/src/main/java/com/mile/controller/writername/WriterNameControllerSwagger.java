package com.mile.controller.writername;

import com.mile.common.resolver.user.UserId;
import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.writername.service.dto.response.WriterNameDescriptionResponse;
import com.mile.writername.service.dto.request.WriterNameDescriptionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "WriterName")
public interface WriterNameControllerSwagger {

    @Operation(summary = "관리자 페이지 멤버 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "멤버 삭제가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 사용자는 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> deleteMember(
            @PathVariable("writerNameId") final Long writerNameId
    );

    @Operation(summary = "필명, 소개글 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "필명 소개글 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 필명이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 필명에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<WriterNameDescriptionResponse>> getWriterNameDescription(
            final Long writerNameId,
            @UserId final Long userId
    );

    @Operation(summary = "소개글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "소개글 수정이 완료 되었습니다."),
                    @ApiResponse(responseCode = "400", description = "1. 소개 글이 입력되지 않았습니다.\n" +
                            "2. 소개 글은 최대 110자 이내로 작성해주세요.\n",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 필명이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 필명에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> updateWriterNameDescription(
            final Long writerNameId,
            final WriterNameDescriptionUpdateRequest request,
            @UserId final Long userId
    );
}

