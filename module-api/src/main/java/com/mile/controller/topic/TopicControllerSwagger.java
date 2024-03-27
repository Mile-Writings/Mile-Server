package com.mile.controller.topic;


import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.topic.service.dto.PostListInTopicResponse;
import com.mile.topic.service.dto.TopicDetailResponse;
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

@Tag(name = "Topic")
public interface TopicControllerSwagger {

    @Operation(summary = "글모임뷰 - 글감 별 글 List")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리별 글 리스트 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "1. 해당 글감이 존재하지 않습니다.\n 2. 해당 글감의 글이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    SuccessResponse<PostListInTopicResponse> getPostListByTopic(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long topicId,
            @PathVariable("topicId") final String topicUrl
    );

    @Operation(summary = "관리자 페이지 - 글감 상세 정보")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글감 상세 정보 조회가 완료되었습니다.",
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
    ResponseEntity<SuccessResponse<TopicDetailResponse>> getTopicDetail(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long topicId,
            @PathVariable("topicId") final String topicUrl
    );
}
