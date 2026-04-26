package com.kwang.climbstyle.code.faq;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FaqErrorCode implements ApiCode {

    FAQ_QUESTION_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "9001", "질문 형식이 올바르지 않습니다."),
    FAQ_ANSWER_INVALID_FORMAT(HttpStatus.BAD_REQUEST,   "9002", "답변 형식이 올바르지 않습니다."),
    FAQ_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,           "9003", "FAQ 입력값 형식이 잘못되었습니다."),
    FAQ_VISIBLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,  "9004", "공개 여부 형식이 올바르지 않습니다."),

    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND,                 "9005", "존재하지 않는 FAQ입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
