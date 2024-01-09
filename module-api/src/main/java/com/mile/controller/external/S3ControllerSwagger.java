package com.mile.controller.external;

import com.mile.aws.utils.PreSignedUrlResponse;
import com.mile.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Image - PreSigned Url", description = "이미지 업로드 할 Url 받기")
public interface S3ControllerSwagger {

    @Operation(summary = "이미지 업로드 presigned url")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이미지를 업로드할 url이 발행되었습니다."),
                    @ApiResponse(responseCode = "500", description = "S3 PRESIGNED URL을 받아오기에 실패했습니다.")
            }
    )
    SuccessResponse<PreSignedUrlResponse> getPreSignedUrl();
}
