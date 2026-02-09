package com.kwang.climbstyle.code.feed;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedSuccessCode implements ApiCode {

    FEED_CREATE_SUCCESS(HttpStatus.OK,      "1000", "피드가 등록되었습니다."),
    FEED_DETAIL_SUCCESS(HttpStatus.OK,      "1000", "피드 상세 조회가 완료되었습니다."),
    FEED_LIKE_CREATE_SUCCESS(HttpStatus.OK, "1000", "좋아요를 등록했습니다."),
    FEED_LIKE_DELETE_SUCCESS(HttpStatus.OK, "1000", "좋아요를 취소했습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
