package com.hanium.memotion.service;

import com.hanium.memotion.domain.Member;

import com.hanium.memotion.dto.auth.TokenDto;
import com.hanium.memotion.dto.auth.request.LoginReqDto;
import com.hanium.memotion.dto.auth.request.SignupReqDto;
import com.hanium.memotion.dto.auth.response.LoginResDto;
import com.hanium.memotion.dto.auth.response.SignupResDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.exception.custom.BadRequestException;
import com.hanium.memotion.exception.custom.InvalidTokenException;

import com.hanium.memotion.repository.MemberRepository;
import com.hanium.memotion.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;


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
                .phone(signupReqDto.getPhone())
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당되는 member_id를 찾을 수 없습니다."));

        // 리프레시 토큰 유효성 검증
        if(!refreshToken.equals(member.getRefreshToken()) || !jwtService.validateToken(refreshToken))
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다.");

        String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(memberId));

        member.renewRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return new LoginResDto(newAccessToken, newRefreshToken);
    }

    // 로그아웃
    @Transactional
    public void logout(String email) {
        Optional<Member> getMember = memberRepository.findByEmail(email);

        if(getMember.isEmpty())
            throw new BadRequestException("잘못된 이메일 입니다.");

        Member member = getMember.get();
        member.refreshTokenExpires();
        memberRepository.save(member);
    }

    public Long getMemberId(String username) {
        return getMemberByUsername(username).getId();
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member username=" + username));
    }
}
