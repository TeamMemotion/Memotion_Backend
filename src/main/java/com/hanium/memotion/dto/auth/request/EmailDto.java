package com.hanium.memotion.dto.auth.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailDto {
    @Email
    private String email;
}
