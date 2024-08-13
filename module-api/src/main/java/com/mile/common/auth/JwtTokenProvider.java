package com.mile.common.auth;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private static final String MEMBER_ID = "memberId";
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 3 * 60 * 1000L;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000L * 14;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @PostConstruct
    protected void init() {
        //base64 라이브러리에서 encodeToString을 이용해서 byte[] 형식을 String 형식으로 변환
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private String getTokenFromHeader(final String token) {
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException(ErrorMessage.UN_LOGIN_EXCEPTION);
        } else if (StringUtils.hasText(token) && !token.startsWith("Bearer ")) {
            throw new BadRequestException(ErrorMessage.BEARER_LOST_ERROR);
        }
        return token.substring("Bearer ".length());
    }

    public String issueAccessToken(final Long userId) {
        return issueToken(userId, ACCESS_TOKEN_EXPIRATION_TIME);
    }


    public String issueRefreshToken(final Long userId) {
        return issueToken(userId, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private String issueToken(
            final Long userId,
            final Long expiredTime
    ) {
        final Date now = new Date();

        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiredTime));  // 만료 시간 설정

        claims.put(MEMBER_ID, userId);
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // Header
                .setClaims(claims) // Claim
                .signWith(getSigningKey()) // Signature
                .compact();
    }

    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes()); //SecretKey 통해 서명 생성
        return Keys.hmacShaKeyFor(encodedKey.getBytes());   //일반적으로 HMAC (Hash-based Message Authentication Code) 알고리즘 사용
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

    public Long getUserFromJwt(final String token) {
        Claims claims = getBody(getTokenFromHeader(token));
        return Long.valueOf(claims.get(MEMBER_ID).toString());
    }
}
