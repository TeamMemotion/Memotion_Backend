package com.hanium.memotion.security;


import com.hanium.memotion.dto.member.TokenDto;
import com.hanium.memotion.exception.custom.BadRequestException;
import com.hanium.memotion.exception.custom.TokenExpiredException;
import com.hanium.memotion.exception.custom.UnauthorizedException;
import com.hanium.memotion.repository.MemberRepository;
import com.hanium.memotion.service.member.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private final MemberRepository memberRepository;
    private final UserDetailServiceImpl userDetailService;

    private Long tokenValidTime = 1000L * 60 * 60; // 1h
    private Long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7d

    // access token 생성
    public String encodeJwtToken(TokenDto tokenDto) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("memotion")
                .setIssuedAt(now)
                .setSubject(tokenDto.getMemberId().toString())
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .claim("memberId", tokenDto.getMemberId())
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    // refresh token 생성
    public String encodeJwtRefreshToken(Long memberId) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(memberId.toString())
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .claim("memberId", memberId)
                .claim("roles", "USER")
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    // JWT 토큰 으로부터 memberId 추출
    public Long getMemberIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                            StandardCharsets.UTF_8)))
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch(Exception e) {
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다");
        }
    }

    // JWT 토큰 인증 정보 조회 (토큰 복호화)
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getMemberIdFromJwtToken(token).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 유효성 + 만료일자 확인
    public Boolean validateToken(String token) {
        Date now = new Date();

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                            StandardCharsets.UTF_8))).parseClaimsJws(token);
//            Long memberId = claims.getBody().get("memberId", Long.class);
//            log.info("validateToken ------- memberId : " + memberId);
            return !claims.getBody().getExpiration().before(new Date(now.getTime()));
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
        }
    }

    // Autorization : Bearer에서 token 추출 (refreshToken, accessToken 포함)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);

        if (bearerToken == null)
            throw new BadRequestException("토큰값이 존재하지 않습니다.");
        else if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        return bearerToken;
    }

    // 토큰 유효 시간 확인
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();

        return (expiration.getTime() - now);
    }
}