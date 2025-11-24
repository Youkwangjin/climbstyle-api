package com.kwang.climbstyle.exception.user;

import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.common.response.ApiErrorResponse;
import com.kwang.climbstyle.domain.user.controller.UserApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(assignableTypes = {UserApiController.class})
public class UserValidException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            UserErrorCode defaultCode = UserErrorCode.USER_DEFAULT_ERROR;

            ApiErrorResponse defaultResponse = ApiErrorResponse.builder()
                    .httpStatus(defaultCode.getHttpStatus())
                    .code(defaultCode.getCode())
                    .message(defaultCode.getMessage())
                    .build();

            return ResponseEntity.status(defaultCode.getHttpStatus()).body(defaultResponse);
        }

        FieldError fieldError = fieldErrors.get(0);

        String fieldName = fieldError.getField();

        UserErrorCode userErrorCode = switch (fieldName) {
            case "userId" -> UserErrorCode.USER_ID_INVALID_FORMAT;
            case "userEmail" -> UserErrorCode.USER_EMAIL_INVALID_FORMAT;
            case "username" -> UserErrorCode.USER_NICKNAME_INVALID_FORMAT;
            default -> UserErrorCode.USER_DEFAULT_ERROR;
        };

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(userErrorCode.getHttpStatus())
                .code(userErrorCode.getCode())
                .message(userErrorCode.getMessage())
                .build();

        return ResponseEntity.status(userErrorCode.getHttpStatus()).body(response);
    }
}
