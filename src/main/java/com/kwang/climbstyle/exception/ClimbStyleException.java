package com.kwang.climbstyle.exception;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 공통 커스텀 런타임 예외
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
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
