package com.hanium.memotion.dto.member.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class KakaoLoginDto {
    @NotNull
    private String username;
    @NotNull
    private String email;
    private String image;
}
