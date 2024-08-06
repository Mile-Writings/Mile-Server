package com.mile.controller.comment;

import com.mile.commentreply.service.dto.ReplyCreateRequest;
import com.mile.common.resolver.user.UserId;
import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
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

@Tag(name = "Comment", description = "댓글 관련 API")
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
    ResponseEntity<SuccessResponse> deleteComment(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long commentId,
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @UserId final Long userId,
            @PathVariable("commentId") final String commentUrl
    );


    @Operation(description = "대댓글 등록 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록이 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 댓글에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 댓글이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> createCommentReply(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long commentId,
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @UserId final Long userId,
            final ReplyCreateRequest createRequest,
            @PathVariable("commentId") final String commentUrl
    );

    @Operation(description = "대댓글 삭제 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "대댓글 삭제가 완료되었습니다."),
                    @ApiResponse(responseCode = "403", description = "해당 사용자는 댓글에 접근 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 댓글이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> deleteCommentReply(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long replyId,
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @UserId final Long userId,
            @PathVariable("replyId") final String replyUrl
    );
}
