package com.mile.user.service;

import com.mile.authentication.UserAuthentication;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.NotFoundException;
import com.mile.external.client.SocialType;
import com.mile.external.client.dto.UserLoginRequest;
import com.mile.external.client.kakao.KakaoSocialService;
import com.mile.external.client.service.dto.UserInfoResponse;
import com.mile.jwt.JwtTokenProvider;
import com.mile.moim.service.MoimService;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import com.mile.user.service.dto.LoginSuccessResponse;
import com.mile.writerName.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoSocialService kakaoSocialService;
    private final MoimService moimService;
    private final WriterNameService writerNameService;

    private static final Long STATIC_MOIM_ID = 1L;

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
        createWriterNameOfUser(user);
        return user.getId();
    }

    private void createWriterNameOfUser(
            final User user
    ) {
        writerNameService.createWriterNameInMile(user, moimService.findById(STATIC_MOIM_ID));
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
        return LoginSuccessResponse.of(
                jwtTokenProvider.issueAccessToken(userAuthentication),
                refreshToken
        );
    }

    @Transactional
    public void deleteUser(
            final Long id
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
                );
        userRepository.delete(user);
    }

    private LoginSuccessResponse getTokenDto(
            final UserInfoResponse userResponse
    ) {
        if (isExistingUser(userResponse.socialId(), userResponse.socialType())) {
            return getTokenByUserId(getBySocialId(userResponse.socialId(), userResponse.socialType()).getId());
        } else {
            Long id = createUser(userResponse);
            return getTokenByUserId(id);
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
