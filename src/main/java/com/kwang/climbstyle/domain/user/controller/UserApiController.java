package com.kwang.climbstyle.domain.user.controller;

import com.kwang.climbstyle.code.user.UserSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.user.dto.request.UserCreateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserIdRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserNickNameRequest;
import com.kwang.climbstyle.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping(value = "/api/v1/users/id/availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> checkUserIdDuplicate(@Valid @RequestBody UserIdRequest request) {
        userService.checkUserIdDuplicate(request);

        return ApiResponseBuilder.success(UserSuccessCode.USER_ID_AVAILABLE);
    }

    @PostMapping(value = "/api/v1/users/email/availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> checkUserEmailDuplicate(@Valid @RequestBody UserEmailRequest request) {
        userService.checkUserEmailDuplicate(request);

        return ApiResponseBuilder.success(UserSuccessCode.USER_EMAIL_AVAILABLE);
    }

    @PostMapping(value = "/api/v1/users/nickname/availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> checkUserNickNameDuplicate(@Valid @RequestBody UserNickNameRequest request) {
        userService.checkUserNickNameDuplicate(request);

        return ApiResponseBuilder.success(UserSuccessCode.USER_NICKNAME_AVAILABLE);
    }

    @PostMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createUser(@Valid @RequestBody UserCreateRequest request) {
        userService.createUser(request);

        return ApiResponseBuilder.success(UserSuccessCode.USER_REGISTER_SUCCESS);
    }
}
