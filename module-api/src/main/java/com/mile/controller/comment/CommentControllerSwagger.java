package com.mile.controller.comment;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Tag(name = "Comment", description = "댓글 관련 API - 현재는 댓글 삭제만 API 해당")
public interface CommentControllerSwagger {

    @Operation(description = "댓글 삭제 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제가 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 댓글에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 댓글이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse deleteComment(
            final Long commentId,
            final Principal principal,
            @PathVariable("commentId") final String commentUrl
    );
}
