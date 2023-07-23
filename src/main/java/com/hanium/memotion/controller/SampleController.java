package com.hanium.memotion.controller;

import com.hanium.memotion.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sample")
public class SampleController {
    @GetMapping("")
    public String sample() {
        return "sample";
    }

    @GetMapping("/loginTest")
    public String loginTest(@AuthenticationPrincipal Member member) {
        return "현재 로그인한 유저의 memberId = " + member.getId();
    }
}
