package com.kwang.climbstyle.code.banner;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 배너 에러 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum BannerErrorCode implements ApiCode {

    BANNER_IMAGE_INVALID(HttpStatus.BAD_REQUEST,   "9001", "이미지 파일이 올바르지 않습니다."),
    BANNER_ORDER_INVALID(HttpStatus.BAD_REQUEST,   "9002", "순서 형식이 올바르지 않습니다."),
    BANNER_VISIBLE_INVALID(HttpStatus.BAD_REQUEST, "9003", "공개 여부 형식이 올바르지 않습니다."),
    BANNER_DEFAULT_ERROR(HttpStatus.BAD_REQUEST,   "9004", "배너 입력값 형식이 잘못되었습니다."),

    BANNER_NOT_FOUND(HttpStatus.NOT_FOUND,         "9005", "존재하지 않는 배너입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
