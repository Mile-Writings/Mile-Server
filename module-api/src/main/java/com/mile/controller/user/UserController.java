package com.mile.controller.user;

import com.mile.common.PrincipalHandler;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.jwt.redis.service.TokenService;
import com.mile.moim.service.dto.MoimListOfUserResponse;
import com.mile.user.service.UserService;
import com.mile.user.service.dto.AccessTokenGetSuccess;
import com.mile.user.service.dto.LoginSuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController implements UserControllerSwagger {

    private final UserService userService;
    private final TokenService tokenService;
    private final PrincipalHandler principalHandler;
    private final static int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
    private final static String REFRESH_TOKEN = "refreshToken";

    @PostMapping("/login")
    @Override
    public ResponseEntity<SuccessResponse<AccessTokenGetSuccess>> login(
            @RequestParam final String authorizationCode,
            @RequestBody @Valid final UserLoginRequest loginRequest,
            HttpServletResponse response
    ) {
        LoginSuccessResponse successResponse = userService.create(authorizationCode, loginRequest);
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, successResponse.refreshToken())
                .maxAge(COOKIE_MAX_AGE)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.LOGIN_SUCCESS, AccessTokenGetSuccess.of(successResponse.accessToken())));
    }


    @GetMapping("/refresh-token")
    @Override
    public SuccessResponse<AccessTokenGetSuccess> refreshToken(
            @RequestParam final String refreshToken
    ) {
        return SuccessResponse.of(SuccessMessage.ISSUE_ACCESS_TOKEN_SUCCESS, userService.refreshToken(refreshToken));
    }


    @PostMapping("/logout")
    @Override
    public SuccessResponse logout(
    ) {
        tokenService.deleteRefreshToken(principalHandler.getUserIdFromPrincipal());
        return SuccessResponse.of(SuccessMessage.LOGOUT_SUCCESS);
    }

    @DeleteMapping("/delete")
    @Override
    public SuccessResponse deleteUser(
    ) {
        userService.deleteUser(principalHandler.getUserIdFromPrincipal());
        return SuccessResponse.of(SuccessMessage.USER_DELETE_SUCCESS);
    }

    @Override
    @GetMapping("/moims")
    public ResponseEntity<SuccessResponse<MoimListOfUserResponse>> getMoimListOfUser() {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_LIST_OF_USER_GET_SUCCESS, userService.getMoimOfUserList(principalHandler.getUserIdFromPrincipal())));
    }
}

