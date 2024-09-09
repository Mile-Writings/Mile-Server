package com.mile.controller.user.facade;


import com.mile.client.SocialType;
import com.mile.client.dto.UserLoginRequest;
import com.mile.common.auth.JwtTokenProvider;
import com.mile.writername.domain.MoimRole;
import com.mile.jwt.service.TokenService;
import com.mile.moim.service.dto.response.MoimListOfUserResponse;
import com.mile.strategy.LoginStrategyManager;
import com.mile.strategy.dto.UserInfoResponse;
import com.mile.user.service.UserService;
import com.mile.user.service.dto.AccessTokenGetSuccess;
import com.mile.user.service.dto.LoginSuccessResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthFacade {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginStrategyManager loginStrategyManager;
    private final TokenService tokenService;

    public AccessTokenGetSuccess refreshToken(
            final String refreshToken
    ) {
        final Long userId = tokenService.findIdByRefreshToken(refreshToken);
        final Map<Long, MoimRole> role = jwtTokenProvider.getJoinedRoleFromJwt(refreshToken);

        return AccessTokenGetSuccess.of(
                jwtTokenProvider.issueAccessToken(userId, role)
        );
    }

    public LoginSuccessResponse create(
            final String authorizationCode,
            final UserLoginRequest loginRequest
    ) {
        return getTokenDto(getUserInfoResponse(authorizationCode, loginRequest));
    }

    public void deleteUser(final Long userId, final String authorizationCode, final UserLoginRequest userLoginRequest) {
        revokeUser(authorizationCode, userLoginRequest);
        userService.deleteUser(userId);
        tokenService.deleteRefreshToken(userId);
    }

    public void deleteRefreshToken(
            final Long id
    ) {
        tokenService.deleteRefreshToken(id);
    }

    private LoginSuccessResponse getTokenDto(
            final UserInfoResponse userResponse
    ) {
        try {
            if (userService.isExistingUser(userResponse.socialId(), userResponse.socialType())) {
                Long userId = userService.getBySocialId(userResponse.socialId(), userResponse.socialType()).getId();
                return getTokenByUserId(userId, userService.getJoinedRoleFromUser(userId));
            } else {
                Long id = userService.createUser(userResponse.socialId(), userResponse.socialType(), userResponse.email());
                return getTokenByUserId(id, new HashMap<>());
            }
        } catch (DataIntegrityViolationException e) {
            Long userId = userService.getBySocialId(userResponse.socialId(), userResponse.socialType()).getId();
            return getTokenByUserId(userId, userService.getJoinedRoleFromUser(userId));
        }
    }

    private void revokeUser(final String authorizationCode, final UserLoginRequest userLoginRequest) {
        switch (userLoginRequest.socialType()) {
            case KAKAO ->
                    loginStrategyManager.getLoginStrategy(SocialType.KAKAO).revokeUser(authorizationCode, userLoginRequest);
            case GOOGLE ->
                    loginStrategyManager.getLoginStrategy(SocialType.GOOGLE).revokeUser(authorizationCode, userLoginRequest);
        }
    }

    public UserInfoResponse getUserInfoResponse(
            final String authorizationCode,
            final UserLoginRequest loginRequest
    ) {
        return switch (loginRequest.socialType()) {
            case KAKAO ->
                    loginStrategyManager.getLoginStrategy(SocialType.KAKAO).login(authorizationCode, loginRequest);
            case GOOGLE ->
                    loginStrategyManager.getLoginStrategy(SocialType.GOOGLE).login(authorizationCode, loginRequest);
        };
    }


    public LoginSuccessResponse getTokenByUserId(
            final Long id,
            final Map<Long, MoimRole> role
    ) {
        String refreshToken = jwtTokenProvider.issueRefreshToken(id, role);
        tokenService.saveRefreshToken(id, refreshToken);
        return LoginSuccessResponse.of(
                jwtTokenProvider.issueAccessToken(id, role),
                refreshToken
        );
    }

    public MoimListOfUserResponse getMoimListOfUser(long userId) {
        return userService.getMoimOfUserList(userId);
    }
}
