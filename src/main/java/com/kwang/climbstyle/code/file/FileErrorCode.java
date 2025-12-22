package com.kwang.climbstyle.code.file;

import com.kwang.climbstyle.common.response.ApiCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ApiCode {
    FILE_EMPTY(HttpStatus.BAD_REQUEST,                      "7400", "파일이 비어 있습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST,              "7401", "파일 크기가 제한을 초과했습니다."),
    FILE_INVALID_NAME(HttpStatus.BAD_REQUEST,               "7402", "잘못된 파일명입니다."),
    FILE_INVALID_TYPE(HttpStatus.BAD_REQUEST,               "7403", "업로드 불가능한 이미지 포맷입니다."),

    FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,        "7404", "파일 데이터가 존재하지 않습니다."),
    FILE_PATH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,       "7405", "파일 업로드 디렉토리가 존재하지 않습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,     "7406", "파일 업로드에 실패하였습니다."),
    FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,     "7407", "파일 삭제 중 문제가 발생했습니다."),
    FILE_DOWNLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,   "7408", "파일 다운로드 중 문제가 발생했습니다."),

    PROFILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,     "7999", "profiles이 지정되지 않았거나 없는 profiles 값입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
