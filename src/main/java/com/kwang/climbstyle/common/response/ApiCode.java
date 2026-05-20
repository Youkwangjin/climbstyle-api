package com.kwang.climbstyle.common.response;

import org.springframework.http.HttpStatus;

/**
 * API 응답 코드 인터페이스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
public interface ApiCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
