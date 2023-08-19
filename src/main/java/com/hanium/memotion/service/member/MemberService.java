package com.hanium.memotion.service.member;

import com.hanium.memotion.domain.member.Member;

import com.hanium.memotion.dto.member.TokenDto;
import com.hanium.memotion.dto.member.request.LoginReqDto;
import com.hanium.memotion.dto.member.request.SignupReqDto;
import com.hanium.memotion.dto.member.response.LoginResDto;
import com.hanium.memotion.dto.member.response.SignupResDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.exception.custom.BadRequestException;
import com.hanium.memotion.exception.custom.InvalidTokenException;

import com.hanium.memotion.exception.custom.UnauthorizedException;
import com.hanium.memotion.repository.MemberRepository;
import com.hanium.memotion.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.hanium.memotion.exception.base.ErrorCode.ALREADY_LOGOUT;
import static com.hanium.memotion.exception.base.ErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignupResDto signup(SignupReqDto signupReqDto) {
        if(!checkEmail(signupReqDto.getEmail()))
            throw new BaseException(ErrorCode.DUPLICATED_EMAIL);

        Member newMember = Member.builder()
                .email(signupReqDto.getEmail())
                .username(signupReqDto.getUsername())
                .password(passwordEncoder.encode(signupReqDto.getPassword()))
                .image(signupReqDto.getImage())
                .build();

        return new SignupResDto(memberRepository.save(newMember));
    }

    // 이메일 중복 검사
    public boolean checkEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent())
            return false;
        return true;
    }

    // 로그인
    @Transactional
    public LoginResDto login(LoginReqDto loginReqDto) {
        Optional<Member> getMember = memberRepository.findByEmail(loginReqDto.getEmail());

        if(getMember.isEmpty())
            throw new BadRequestException("잘못된 이메일 입니다.");

        Member member = getMember.get();
        if(!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword()))
            throw new BadRequestException("잘못된 비밀번호 입니다.");

        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getId());

        member.renewRefreshToken(newRefreshToken);
        memberRepository.save(member);
        return new LoginResDto(newAccessToken, newRefreshToken);
    }

    // 액세스 토큰 재발급
    @Transactional
    public LoginResDto regenerateAccessToken(String refreshToken) {
        Long memberId = jwtService.getMemberIdFromJwtToken(refreshToken);
        log.info("memberId : " + memberId);

        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isEmpty())
            throw new BaseException(MEMBER_NOT_FOUND);

        Member member = getMember.get();
        if(!refreshToken.equals(member.getRefreshToken()))
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다");

        String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(memberId));

        member.renewRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return new LoginResDto(newAccessToken, newRefreshToken);
    }

    // 로그아웃
    @Transactional
    public String logout(String accessToken) {
        if(!jwtService.validateToken(accessToken))
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");

        Long memberId = jwtService.getMemberIdFromJwtToken(accessToken);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isEmpty())
            throw new BaseException(MEMBER_NOT_FOUND);

        Member member = getMember.get();
        if(member.getRefreshToken().equals(""))
            throw new BaseException(ALREADY_LOGOUT);

        member.refreshTokenExpires();
        memberRepository.save(member);

        return "로그아웃 성공";
    }

    public Long getMemberId(String username) {
        return getMemberByUsername(username).getId();
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member username=" + username));
    }
}
