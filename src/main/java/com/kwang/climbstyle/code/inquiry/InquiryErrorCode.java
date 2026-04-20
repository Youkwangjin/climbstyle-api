package com.kwang.climbstyle.code.inquiry;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InquiryErrorCode implements ApiCode {

    INQUIRY_TITLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST,   "8001", "문의 제목 형식이 올바르지 않습니다."),
    INQUIRY_CONTENT_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "8002", "문의 내용 형식이 올바르지 않습니다."),
    INQUIRY_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,          "8003", "문의 입력값 형식이 잘못되었습니다."),

    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND,                "8404", "존재하지 않는 문의입니다."),
    INQUIRY_ALREADY_DELETED(HttpStatus.BAD_REQUEST,        "8005", "이미 삭제된 문의입니다."),
    INQUIRY_NOT_MODIFIABLE(HttpStatus.BAD_REQUEST,         "8006", "대기 상태에서만 수정이 가능합니다."),
    INQUIRY_NOT_DELETABLE(HttpStatus.BAD_REQUEST,          "8007", "처리중 상태에서는 삭제가 불가능합니다."),

    INQUIRY_FILE_NOT_FOUND(HttpStatus.NOT_FOUND,           "8008", "해당 문의내역에 등록된 파일이 없습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
