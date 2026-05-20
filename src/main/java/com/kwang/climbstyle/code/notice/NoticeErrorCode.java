package com.kwang.climbstyle.code.notice;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 공지사항 에러 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements ApiCode {
    NOTICE_CATEGORY_INVALID_FORMAT(HttpStatus.BAD_REQUEST,    "6001", "공지사항 카테고리 형식이 올바르지 않습니다."),
    NOTICE_TITLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,       "6002", "공지사항 제목 형식이 올바르지 않습니다."),
    NOTICE_CONTENT_INVALID_FORMAT(HttpStatus.BAD_REQUEST,     "6003", "공지사항 내용 형식이 올바르지 않습니다."),
    NOTICE_PIN_INVALID_FORMAT(HttpStatus.BAD_REQUEST,         "6004", "공지사항 고정 여부 형식이 올바르지 않습니다."),
    NOTICE_VISIBLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,     "6005", "공지사항 공개 여부 형식이 올바르지 않습니다."),
    NOTICE_FILE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST,        "6006", "공지사항 첨부파일은 최대 5개까지 첨부 가능합니다."),
    NOTICE_FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST,         "6007", "첨부파일 크기는 10MB 이하만 가능합니다."),
    NOTICE_FILE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,        "6008", "허용되지 않는 파일 형식입니다."),
    NOTICE_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,              "6009", "공지사항 입력값 형식이 잘못되었습니다."),

    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND,                    "6404", "존재하지 않는 공지사항입니다."),
    NOTICE_FILE_NOT_FOUND(HttpStatus.NOT_FOUND,               "6404", "해당 공지사항에 등록된 파일이 없습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
