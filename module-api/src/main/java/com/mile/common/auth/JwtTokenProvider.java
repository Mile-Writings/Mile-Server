package com.mile.common.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.UnauthorizedException;
import com.mile.writername.domain.MoimRole;
import com.mile.writername.service.vo.WriterNameInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.misc.Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final ObjectMapper objectMapper;
    private static final String MEMBER_ID = "memberId";
    private static final String JOINED_ROLE = "joinedRole";
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 48 * 60 * 60 * 1000L;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000L * 30;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @PostConstruct
    protected void init() {
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private String getTokenFromHeader(final String token) {
        if (!StringUtils.hasText(token) || token.equals("Bearer null") || token.equals("Bearer ")) {
            throw new UnauthorizedException(ErrorMessage.UN_LOGIN_EXCEPTION);
        } else if (StringUtils.hasText(token) && !token.startsWith("Bearer ")) {
            throw new BadRequestException(ErrorMessage.BEARER_LOST_ERROR);
        }
        return token.substring("Bearer ".length());
    }

    public String issueAccessToken(final Long userId, final Map<Long, WriterNameInfo> joinedRole) {
        return issueToken(userId, joinedRole, ACCESS_TOKEN_EXPIRATION_TIME);
    }


    public String issueRefreshToken(final Long userId, final Map<Long, WriterNameInfo> joinedRole) {
        return issueToken(userId, joinedRole, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private String issueToken(
            final Long userId,
            final Map<Long, WriterNameInfo> role,
            final Long expiredTime
    ) {
        final Date now = new Date();

        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiredTime));

        claims.put(MEMBER_ID, userId);
        claims.put(JOINED_ROLE, role);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public JwtValidationType validateToken(final String token) {
        try {
            final Claims claims = getBody(getTokenFromHeader(token));
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException ex) {
            return JwtValidationType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException ex) {
            return JwtValidationType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException ex) {
            return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException ex) {
            return JwtValidationType.EMPTY_JWT;
        }
    }

    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserFromAuthHeader(final String token) {
        Claims claims = getBody(getTokenFromHeader(token));
        return Long.valueOf(claims.get(MEMBER_ID).toString());
    }

    public HashMap<Long, WriterNameInfo> getJoinedRoleFromHeader(final String token) {
        return getJoinedRoleFromJwt(getTokenFromHeader(token));
    }

    public HashMap<Long, WriterNameInfo> getJoinedRoleFromJwt(final String token) {
        Claims claims = getBody(token);
        Object joinedRole = claims.get(JOINED_ROLE);
        HashMap<Long, WriterNameInfo> roleMap = objectMapper.convertValue(joinedRole, new TypeReference<HashMap<Long, WriterNameInfo>>() {});
        return roleMap;
    }
}
