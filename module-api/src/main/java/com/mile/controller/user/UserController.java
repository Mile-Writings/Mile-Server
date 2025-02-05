package com.mile.controller.user;

import com.mile.client.dto.UserLoginRequest;
import com.mile.common.auth.annotation.UserAuthAnnotation;
import com.mile.common.auth.annotation.UserAuthenticationType;
import com.mile.common.resolver.user.UserId;
import com.mile.controller.user.facade.AuthFacade;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.moim.service.dto.response.MoimListOfUserResponse;
import com.mile.user.service.dto.AccessTokenGetSuccess;
import com.mile.user.service.dto.LoginSuccessResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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

    private final AuthFacade authFacade;
    private final static Long COOKIE_MAX_AGE = 60 * 60 * 24 * 1000L * 14;
    private final static String REFRESH_TOKEN = "M-RID";

    @PostMapping("/login")
    @Override
    public ResponseEntity<SuccessResponse<AccessTokenGetSuccess>> login(
            @RequestParam final String authorizationCode,
            @RequestBody @Valid final UserLoginRequest loginRequest,
            HttpServletResponse response
    ) {
        LoginSuccessResponse successResponse = authFacade.create(authorizationCode, loginRequest);
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, successResponse.refreshToken())
                .maxAge(COOKIE_MAX_AGE)
                .path("/")
                .secure(true)
                .sameSite("Strict")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.LOGIN_SUCCESS, AccessTokenGetSuccess.of(successResponse.accessToken())));
    }


    @GetMapping("/refresh-token")
    @Override
    public SuccessResponse<AccessTokenGetSuccess> refreshToken(
            @CookieValue(name = REFRESH_TOKEN) Cookie cookie
    ) {
        String refreshToken = cookie.getValue();
        return SuccessResponse.of(SuccessMessage.ISSUE_ACCESS_TOKEN_SUCCESS, authFacade.refreshToken(refreshToken));
    }


    @PostMapping("/logout")
    @Override
    public SuccessResponse logout(
            @UserId final Long userId
    ) {
        authFacade.deleteRefreshToken(userId);
        return SuccessResponse.of(SuccessMessage.LOGOUT_SUCCESS);
    }

    @DeleteMapping("/delete")
    @Override
    public SuccessResponse deleteUser(
            @UserId final Long userId,
            @RequestParam final String authorizationCode,
            @RequestBody final UserLoginRequest userLoginRequest
    ) {
        authFacade.deleteUser(userId, authorizationCode, userLoginRequest);
        return SuccessResponse.of(SuccessMessage.USER_DELETE_SUCCESS);
    }

    @Override
    @GetMapping("/moims")
    public ResponseEntity<SuccessResponse<MoimListOfUserResponse>> getMoimListOfUser(
            @UserId final Long userId
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_LIST_OF_USER_GET_SUCCESS, authFacade.getMoimListOfUser(userId)));
    }
}

