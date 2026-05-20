package com.kwang.climbstyle.code.notice;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 공지사항 성공 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum NoticeSuccessCode implements ApiCode {

    NOTICE_CREATE_SUCCESS(HttpStatus.OK,            "1000", "공지사항이 등록되었습니다."),
    NOTICE_IMAGE_UPLOAD_SUCCESS(HttpStatus.OK,      "1000", "이미지가 업로드되었습니다."),
    NOTICE_DELETE_SUCCESS(HttpStatus.OK,            "1000", "공지사항이 삭제되었습니다."),
    NOTICE_DETAIL_SUCCESS(HttpStatus.OK,            "1000", "공지사항을 불러왔습니다."),
    NOTICE_UPDATE_SUCCESS(HttpStatus.OK,            "1000", "공지사항이 수정되었습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
