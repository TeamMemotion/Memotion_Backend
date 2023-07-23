package com.hanium.memotion.exception.custom;

import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import lombok.Getter;


@Getter
public class UnauthorizedException extends BaseException {

    String message;

    public UnauthorizedException(String message) {
        super(ErrorCode._UNAUTHORIZED);
        this.message = message;
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
