package com.hanium.memotion.dto.member;

import lombok.Getter;

@Getter
public class TokenDto {

    private final Long memberId;

    public TokenDto(Long memberId) {
        this.memberId = memberId;
    }
}