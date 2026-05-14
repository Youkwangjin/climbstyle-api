package com.kwang.climbstyle.code.feed;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements ApiCode {
    FEED_TITLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,       "5001", "피드 제목 형식이 올바르지 않습니다."),
    FEED_CONTENT_INVALID_FORMAT(HttpStatus.BAD_REQUEST,     "5002", "피드 내용 형식이 올바르지 않습니다."),
    FEED_VISIBLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,     "5003", "공개 범위 형식이 올바르지 않습니다."),
    FEED_COMMENT_INVALID_FORMAT(HttpStatus.BAD_REQUEST,     "5004", "피드 댓글 형식이 올바르지 않습니다."),
    FEED_IMAGE_REQUIRED(HttpStatus.BAD_REQUEST,             "5005", "피드 이미지를 최소 1개 이상 첨부해주세요."),
    FEED_IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST,       "5006", "피드 이미지는 최대 10개까지 첨부 가능합니다."),
    FEED_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,              "5007", "피드 입력값 형식이 잘못되었습니다."),

    FEED_NOT_FOUND(HttpStatus.NOT_FOUND,                    "5404", "존재하지 않는 피드입니다."),
    FEED_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,            "5404", "존재하지 않는 댓글입니다."),
    FEED_FILE_NOT_FOUND(HttpStatus.NOT_FOUND,               "5404", "해당 피드에 등록된 파일이 없습니다."),

    FEED_LIKE_HIDDEN(HttpStatus.FORBIDDEN,                  "5403", "좋아요 수가 비공개 설정된 피드입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
