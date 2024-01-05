package com.mile.controller.user;

import com.mile.common.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.token.service.TokenService;
import com.mile.user.serivce.UserService;
import com.mile.user.serivce.dto.AccessTokenGetSuccess;
import com.mile.user.serivce.dto.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController implements UserControllerSwagger {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Override
    public SuccessResponse<LoginSuccessResponse> login(
            @RequestParam final String authorizationCode,
            @RequestBody final UserLoginRequest loginRequest
    ) {
        return SuccessResponse.of(SuccessMessage.LOGIN_SUCCESS, userService.create(authorizationCode, loginRequest));
    }

    @GetMapping("/token-refresh")
    @Override
    public SuccessResponse<AccessTokenGetSuccess> refreshToken(
            @RequestParam final String refreshToken
    ) {
        return SuccessResponse.of(SuccessMessage.ISSUE_ACCESS_TOKEN_SUCCESS, userService.refreshToken(refreshToken));
    }

    @DeleteMapping("/delete")
    @Override
    public SuccessResponse deleteUser(
            final Principal principal
    ) {
        userService.deleteUser(Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.USER_DELETE_SUCCESS);
    }

    @PostMapping("/logout")
    @Override
    public SuccessResponse logout(
            final Principal principal
    ) {
        tokenService.deleteRefreshToken(Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.LOGOUT_SUCCESS);
    }
}

