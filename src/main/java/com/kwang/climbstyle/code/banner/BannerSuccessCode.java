package com.kwang.climbstyle.code.banner;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 배너 성공 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum BannerSuccessCode implements ApiCode {

    BANNER_CREATE_SUCCESS(HttpStatus.OK, "1000", "배너가 등록되었습니다."),
    BANNER_UPDATE_SUCCESS(HttpStatus.OK, "1000", "배너가 수정되었습니다."),
    BANNER_DELETE_SUCCESS(HttpStatus.OK, "1000", "배너가 삭제되었습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
