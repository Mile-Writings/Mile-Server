package com.mile.controller.user;

import com.mile.dto.ErrorResponse;
import com.mile.dto.SuccessResponse;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.moim.service.dto.MoimListOfUserResponse;
import com.mile.user.service.dto.AccessTokenGetSuccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<SuccessResponse<AccessTokenGetSuccess>> login(
            @RequestParam final String authorizationCode,
            @RequestBody final UserLoginRequest loginRequest,
            HttpServletResponse response
    );

    @Operation(summary = "액세스 토큰 재발급")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "액세스 토큰 재발급이 완료되었습니다.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "401", description = "리프레시 토큰이 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 유저의 리프레시 토큰이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse<AccessTokenGetSuccess> refreshToken(
            @RequestParam final String refreshToken
    );

    @Operation(summary = "로그아웃")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "로그아웃이 완료되었습니다.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 유저의 리프레시 토큰이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    SuccessResponse logout();

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
    SuccessResponse deleteUser();


    @Operation(summary = "유저 글모임 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "글모임 리스트 조회가 조회되었습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "로그인 후 이용해주세요.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<SuccessResponse<MoimListOfUserResponse>> getMoimListOfUser();
}
