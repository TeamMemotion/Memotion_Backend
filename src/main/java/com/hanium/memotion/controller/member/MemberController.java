package com.hanium.memotion.controller.member;

import com.hanium.memotion.domain.member.Member;
import com.hanium.memotion.dto.member.request.EmailDto;
import com.hanium.memotion.dto.member.request.LoginReqDto;
import com.hanium.memotion.dto.member.request.SignupReqDto;
import com.hanium.memotion.dto.member.response.LoginResDto;
import com.hanium.memotion.dto.member.response.ProfileResDto;
import com.hanium.memotion.dto.member.response.SignupResDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.exception.custom.UnauthorizedException;
import com.hanium.memotion.service.member.MailService;
import com.hanium.memotion.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;

    // 회원가입
    @PostMapping("/signup")
    public BaseResponse<SignupResDto> signup(@RequestBody @Validated SignupReqDto signupReqDto) throws BaseException {
        SignupResDto signupResDto = memberService.signup(signupReqDto);
        return BaseResponse.onSuccess(signupResDto);
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<LoginResDto> login(@RequestBody @Validated LoginReqDto loginReqDto) throws BaseException {
        LoginResDto loginResDto = memberService.login(loginReqDto);
        return BaseResponse.onSuccess(loginResDto);
    }

    // 액세스 토큰 발급
    @PostMapping("/regenerate-token")
    public BaseResponse<LoginResDto> regenerateAccessToken(HttpServletRequest request) throws BaseException {
        String refreshToken = request.getHeader("Authorization");
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            LoginResDto result = memberService.regenerateAccessToken(refreshToken.substring(7));
            return BaseResponse.onSuccess(result);
        } else
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
    }

    // 이메일로 비밀번호 찾기 (랜덤하게 생성한 임시 비밀번호 전송)
    @PostMapping("/check-password")
    public BaseResponse<String> checkPassword(@RequestBody EmailDto emailDto) throws BaseException {
        String password = mailService.sendPasswordMail(emailDto.getEmail());
        return BaseResponse.onSuccess(password);
    }

    // 로그아웃 -> 토큰 만료 (헤더로 refreshToken)
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String result = memberService.logout(token.substring(7));
            return BaseResponse.onSuccess(result);
        } else
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
    }

    // 이메일 인증
    @PostMapping("/check-email")
    public BaseResponse<String> checkEmail(@RequestBody EmailDto emailDto) throws BaseException {
        String code = mailService.sendCodeMail(emailDto.getEmail());
        return BaseResponse.onSuccess(code);
    }

    // 프로필 조회
    @GetMapping("/profile")
    public BaseResponse<ProfileResDto> getProfile(@AuthenticationPrincipal Member member) {
        ProfileResDto profileResDto = memberService.getProfile(member.getId());
        return BaseResponse.onSuccess(profileResDto);
    }
}
