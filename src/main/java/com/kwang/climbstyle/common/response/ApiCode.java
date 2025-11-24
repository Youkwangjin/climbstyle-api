package com.kwang.climbstyle.common.response;

import org.springframework.http.HttpStatus;

public interface ApiCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
