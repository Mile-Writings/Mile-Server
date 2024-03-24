package com.mile.user.service;

import com.mile.authentication.UserAuthentication;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.NotFoundException;
import com.mile.exception.model.UnauthorizedException;
import com.mile.external.client.SocialType;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.external.client.kakao.KakaoSocialService;
import com.mile.external.client.service.dto.UserInfoResponse;
import com.mile.jwt.JwtTokenProvider;
import com.mile.jwt.redis.service.TokenService;
import com.mile.moim.service.MoimService;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import com.mile.user.service.dto.AccessTokenGetSuccess;
import com.mile.user.service.dto.LoginSuccessResponse;
import com.mile.writername.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoSocialService kakaoSocialService;
    private final TokenService tokenService;
    private final WriterNameService writerNameService;


    public LoginSuccessResponse create(
            final String authorizationCode,
            final UserLoginRequest loginRequest
    ) {
        return getTokenDto(getUserInfoResponse(authorizationCode, loginRequest));
    }

    public UserInfoResponse getUserInfoResponse(
            final String authorizationCode,
            final UserLoginRequest loginRequest
    ) {
        switch (loginRequest.socialType()) {
            case KAKAO:
                return kakaoSocialService.login(authorizationCode, loginRequest);
            default:
                throw new BadRequestException(ErrorMessage.SOCIAL_TYPE_BAD_REQUEST);
        }
    }

    public Long createUser(final UserInfoResponse userResponse) {
        User user = User.of(
                userResponse.socialId(),
                userResponse.email(),
                userResponse.socialType()
        );
        userRepository.saveAndFlush(user);
        return user.getId();
    }

    public User getBySocialId(
            final Long socialId,
            final SocialType socialType
    ) {
        User user = userRepository.findBySocialTypeAndSocialId(socialId, socialType).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
        return user;
    }

    public AccessTokenGetSuccess refreshToken(
            final String refreshToken
    ) {
        Long userId = jwtTokenProvider.getUserFromJwt(refreshToken);
        if (!userId.equals(tokenService.findIdByRefreshToken(refreshToken))) {
            throw new UnauthorizedException(ErrorMessage.TOKEN_INCORRECT_ERROR);
        }
        UserAuthentication userAuthentication = new UserAuthentication(userId, null, null);
        return AccessTokenGetSuccess.of(
                jwtTokenProvider.issueAccessToken(userAuthentication)
        );
    }

    public boolean isExistingUser(
            final Long socialId,
            final SocialType socialType
    ) {
        return userRepository.findBySocialTypeAndSocialId(socialId, socialType).isPresent();
    }

    public LoginSuccessResponse getTokenByUserId(
            final Long id
    ) {
        UserAuthentication userAuthentication = new UserAuthentication(id, null, null);
        String refreshToken = jwtTokenProvider.issueRefreshToken(userAuthentication);
        tokenService.saveRefreshToken(id, refreshToken);
        return LoginSuccessResponse.of(
                jwtTokenProvider.issueAccessToken(userAuthentication),
                refreshToken
        );
    }

    @Transactional
    public void deleteUser(
            final Long id
    ) {
        writerNameService.deleteWriterName(id);
        delete(id);
        deleteUserToken(id);
    }

    private void delete(
            final Long id
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
                );
        userRepository.delete(user);
    }

    private void deleteUserToken(
            final Long id
    ) {
        tokenService.deleteRefreshToken(id);
    }

    private LoginSuccessResponse getTokenDto(
            final UserInfoResponse userResponse
    ) {
        try {
            if (isExistingUser(userResponse.socialId(), userResponse.socialType())) {
                return getTokenByUserId(getBySocialId(userResponse.socialId(), userResponse.socialType()).getId());
            } else {
                Long id = createUser(userResponse);
                return getTokenByUserId(id);
            }
        } catch (DataIntegrityViolationException e) {
            return getTokenByUserId(getBySocialId(userResponse.socialId(), userResponse.socialType()).getId());
        }
    }

    public User findById(
            Long userId
    ) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
                );
    }
}
