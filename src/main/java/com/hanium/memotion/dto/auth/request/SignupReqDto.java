package com.hanium.memotion.dto.auth.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupReqDto {
    @Size(min = 2, max = 45)
    private String username;
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
            message = "비밀번호는 8~20자 영문 대/소문자, 숫자를 사용하세요.")
    private String password;
    @NotNull
    private String phone;
    private String image;
}
