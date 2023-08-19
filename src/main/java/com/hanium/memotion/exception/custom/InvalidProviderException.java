package com.hanium.memotion.exception.custom;

import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidProviderException extends BaseException {

    private String message;

    public InvalidProviderException(String message){
        super(ErrorCode.INVALID_PROVIDER, message);
        this.message = message;
    }

    public InvalidProviderException(ErrorCode errorCode) {
        super(errorCode);
    }
}
