package com.hanium.memotion.exception.custom;

import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {
    private String message;

    public BadRequestException(String message) {
        super(ErrorCode._BAD_REQUEST, message);
        this.message = message;
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
