package com.mile.controller.user;

import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.user.service.UserService;
import com.mile.user.service.dto.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @PostMapping("/login")
    @Override
    public SuccessResponse<LoginSuccessResponse> login(
            @RequestParam final String authorizationCode,
            @RequestBody final UserLoginRequest loginRequest
    ) {
        return SuccessResponse.of(SuccessMessage.LOGIN_SUCCESS, userService.create(authorizationCode, loginRequest));
    }


    @DeleteMapping("/delete")
    @Override
    public SuccessResponse deleteUser(
            final Principal principal
    ) {
        userService.deleteUser(Long.valueOf(principal.getName()));
        return SuccessResponse.of(SuccessMessage.USER_DELETE_SUCCESS);
    }


}

