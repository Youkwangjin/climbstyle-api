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
public class UserValidationException {

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

        UserErrorCode userErrorCode = getUserErrorCode(fieldErrors);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(userErrorCode.getHttpStatus())
                .code(userErrorCode.getCode())
                .message(userErrorCode.getMessage())
                .build();

        return ResponseEntity.status(userErrorCode.getHttpStatus()).body(response);
    }

    private static UserErrorCode getUserErrorCode(List<FieldError> fieldErrors) {
        String[] fieldPriority = {"userId",
                                  "userPassword",
                                  "userNm",
                                  "userEmail",
                                  "userNickName",
                                  "userProfileImg",
                                  "userIntro",};

        for (String priorityField : fieldPriority) {
            for (FieldError fieldError : fieldErrors) {
                if (fieldError.getField().equals(priorityField)) {
                    String fieldName = fieldError.getField();
                    switch (fieldName) {
                        case "userId": return UserErrorCode.USER_ID_INVALID_FORMAT;
                        case "userPassword": return UserErrorCode.USER_PASSWORD_INVALID_FORMAT;
                        case "userNm": return UserErrorCode.USER_NAME_INVALID_FORMAT;
                        case "userEmail": return UserErrorCode.USER_EMAIL_INVALID_FORMAT;
                        case "userNickName": return UserErrorCode.USER_NICKNAME_INVALID_FORMAT;
                        case "userIntro": return UserErrorCode.USER_INTRO_INVALID_FORMAT;
                    }
                }
            }
        }

        return UserErrorCode.USER_DEFAULT_ERROR;
    }
}
