package com.kwang.climbstyle.code.inquiry;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InquirySuccessCode implements ApiCode {

    INQUIRY_CREATE_SUCCESS(HttpStatus.OK, "1000", "문의가 등록되었습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
