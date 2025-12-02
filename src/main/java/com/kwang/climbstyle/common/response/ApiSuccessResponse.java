package com.kwang.climbstyle.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiSuccessResponse<T> {

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

    private final T data;

    @Builder
    public ApiSuccessResponse(HttpStatus httpStatus, String code, String message, T data) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiSuccessResponse<T> of(HttpStatus httpStatus, String code, String message, T data) {
        return new ApiSuccessResponse<>(httpStatus, code, message, data);
    }
}
