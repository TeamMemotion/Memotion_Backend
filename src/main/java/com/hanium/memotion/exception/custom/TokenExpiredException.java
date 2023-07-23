package com.hanium.memotion.exception.custom;

import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import lombok.Getter;


@Getter
public class TokenExpiredException extends BaseException {

    private String message;

    public TokenExpiredException(String message){
        super(ErrorCode._BAD_REQUEST,message);
        this.message = message;
    }

    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
