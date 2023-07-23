package com.hanium.memotion.controller;

import com.hanium.memotion.dto.auth.request.EmailDto;
import com.hanium.memotion.dto.auth.request.LoginReqDto;
import com.hanium.memotion.dto.auth.request.SignupReqDto;
import com.hanium.memotion.dto.auth.response.LoginResDto;
import com.hanium.memotion.dto.auth.response.SignupResDto;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.service.MailService;
import com.hanium.memotion.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<LoginResDto> regenerateAccessToken(@RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken) throws BaseException {
        // refreshToken 디코딩 + 유효성 검사
        LoginResDto loginResDto = memberService.regenerateAccessToken(refreshToken);
        return BaseResponse.onSuccess(loginResDto);
    }

    // 이메일로 비밀번호 찾기 (랜덤하게 생성한 임시 비밀번호 전송)
    @PostMapping("/check-password")
    public BaseResponse<String> checkPassword(@RequestBody EmailDto emailDto) throws BaseException {
        String password = mailService.sendMail(emailDto.getEmail());
        return BaseResponse.onSuccess(password);
    }
}
