package com.kwang.climbstyle.code.inquiry;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InquirySuccessCode implements ApiCode {

    INQUIRY_CREATE_SUCCESS(HttpStatus.OK,        "1000", "문의내역이 등록되었습니다."),
    INQUIRY_UPDATE_SUCCESS(HttpStatus.OK,        "1000", "문의내역이 수정되었습니다."),
    INQUIRY_STATUS_UPDATE_SUCCESS(HttpStatus.OK, "1000", "문의 상태가 변경되었습니다."),
    INQUIRY_DELETE_SUCCESS(HttpStatus.OK,        "1000", "문의내역이 삭제되었습니다."),
    INQUIRY_ANSWER_SAVE_SUCCESS(HttpStatus.OK,   "1000", "답변이 등록되었습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
