package com.hanium.memotion.exception.custom;

import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidTokenException extends BaseException {

    private String message;

    public InvalidTokenException(String message){
        super(ErrorCode.INVALID_JWT, message);
        this.message = message;
    }

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
