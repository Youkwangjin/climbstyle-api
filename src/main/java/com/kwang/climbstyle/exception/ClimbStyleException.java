package com.kwang.climbstyle.exception;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClimbStyleException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

    public ClimbStyleException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.httpStatus = apiCode.getHttpStatus();
        this.code = apiCode.getCode();
        this.message = apiCode.getMessage();
    }
}
