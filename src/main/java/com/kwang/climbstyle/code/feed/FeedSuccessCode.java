package com.kwang.climbstyle.code.feed;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedSuccessCode implements ApiCode {

    FEED_CREATE_SUCCESS(HttpStatus.OK, "1000", "피드가 등록되었습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
