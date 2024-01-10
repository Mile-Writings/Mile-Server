package com.mile.controller.user;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.user.service.dto.AccessTokenGetSuccess;
import com.mile.user.service.dto.LoginSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Tag(name = "User", description = "User 관련 API")
public interface UserControllerSwagger {
    @Operation(summary = "소셜 로그인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "소셜 로그인이 완료되었습니다."),
                    @ApiResponse(responseCode = "400", description =
                            "1. 요청한 값이 유효하지 않습니다.\n" + "2. 인가 코드가 만료되었습니다.\n",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<LoginSuccessResponse> login(
            @RequestParam final String authorizationCode,
            @RequestBody final UserLoginRequest loginRequest
    );

    @Operation(summary = "회원 탈퇴")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원 삭제가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description =
                            "1. 해당 유저의 리프레시 토큰이 존재하지 않습니다.\n" +
                                    "2. 해당 유저는 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse deleteUser(
            final Principal principal
    );

}
