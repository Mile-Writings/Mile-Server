package com.mile.controller.writername;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
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
                    @ApiResponse(responseCode = "404", description = "해당 모임은 존재하지 않습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse> deleteMember(
            @PathVariable("writerNameId") final Long writerNameId
    );
}

