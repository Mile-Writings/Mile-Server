package com.mile.controller.recommend;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.recommend.service.dto.RecommendResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Recommend Topic", description = "메인 뷰 글감 추천 API")
public interface RecommendControllerSwagger {
    @Operation(summary = "메인 뷰 글감 추천 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "추천 글감이 조회되었습니다."),
                    @ApiResponse(responseCode = "404", description = "추천 글감을 받아오는데 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<RecommendResponse> getRecommendationDaily();
}