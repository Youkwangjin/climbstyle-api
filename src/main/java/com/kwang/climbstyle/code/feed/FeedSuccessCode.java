package com.kwang.climbstyle.code.feed;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 피드 성공 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum FeedSuccessCode implements ApiCode {

    FEED_LIST_SUCCESS(HttpStatus.OK,                "1000", "피드 목록을 불러왔습니다."),
    FEED_CREATE_SUCCESS(HttpStatus.OK,              "1000", "피드가 등록되었습니다."),
    FEED_DETAIL_SUCCESS(HttpStatus.OK,              "1000", "피드를 불러왔습니다."),
    FEED_LIKE_DETAIL_SUCCESS(HttpStatus.OK,         "1000", "피드 좋아요 목록을 불러왔습니다."),
    FEED_UPDATE_SUCCESS(HttpStatus.OK,              "1000", "피드가 수정되었습니다."),
    FEED_DELETE_SUCCESS(HttpStatus.OK,              "1000", "피드가 삭제되었습니다."),
    FEED_LIKE_CREATE_SUCCESS(HttpStatus.OK,         "1000", "좋아요를 등록했습니다."),
    FEED_LIKE_DELETE_SUCCESS(HttpStatus.OK,         "1000", "좋아요를 취소했습니다."),
    FEED_COMMENT_CREATE_SUCCESS(HttpStatus.OK,      "1000", "댓글을 등록했습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
