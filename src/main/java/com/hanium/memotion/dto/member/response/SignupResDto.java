package com.hanium.memotion.dto.member.response;

import com.hanium.memotion.domain.member.Member;
import lombok.Data;

@Data
public class SignupResDto {
    private Long memberId;
    private String email;

    public SignupResDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
    }
}
