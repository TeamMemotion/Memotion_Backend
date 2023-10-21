package com.hanium.memotion.dto.member.request;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class PasswordDto {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
            message = "비밀번호는 8~20자 영문 대/소문자, 숫자를 사용하세요.")
    private String password;
}
