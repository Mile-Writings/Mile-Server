package com.mile.common.auth;

import com.mile.writername.service.vo.WriterNameInfo;
import com.mile.jwt.service.TokenService;
import com.mile.writername.domain.MoimRole;
import com.mile.writername.service.WriterNameRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUpdater {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final WriterNameRetriever writerNameRetriever;

    public String setAccessToken(final Long userId, final Long moimId, final Long writerNameId, final MoimRole moimRole) {

        Map<Long, WriterNameInfo> moimRoleMap = writerNameRetriever.getJoinedRoleFromUserId(userId);

        tokenService.deleteRefreshToken(userId);

        moimRoleMap.put(moimId, WriterNameInfo.of(writerNameId, moimRole));

        String newAccessToken = jwtTokenProvider.issueAccessToken(userId, moimRoleMap);
        String newRefreshToken = jwtTokenProvider.issueRefreshToken(userId, moimRoleMap);

        tokenService.saveRefreshToken(userId, newRefreshToken);

        return newAccessToken;
    }

}
