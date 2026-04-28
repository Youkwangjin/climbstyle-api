package com.kwang.climbstyle.domain.user.controller;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.code.user.UserSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.user.dto.request.*;
import com.kwang.climbstyle.domain.user.service.EmailVerificationService;
import com.kwang.climbstyle.domain.user.service.UserService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping(value = "/api/v1/users/id/availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> checkUserIdDuplicate(@Valid @RequestBody UserIdRequest request) {
        userService.checkUserIdDuplicate(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_ID_AVAILABLE);
    }

    @PostMapping(value = "/api/v1/users/email/verification-code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> sendEmailVerificationCode(@Valid @RequestBody UserEmailRequest request, HttpSession session) {
        emailVerificationService.sendVerificationCode(request, session);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_EMAIL_VERIFICATION_CODE_SENT);
    }

    @PostMapping(value = "/api/v1/users/email/verification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> verifyEmailCode(@Valid @RequestBody UserEmailVerificationRequest request, HttpSession session) {
        emailVerificationService.verifyCode(request, session);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_EMAIL_VERIFIED);
    }

    @PostMapping(value = "/api/v1/users/nickname/availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> checkUserNickNameDuplicate(@Valid @RequestBody UserNicknameRequest request) {
        userService.checkUserNicknameDuplicate(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_NICKNAME_AVAILABLE);
    }

    @PostMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createUser(@Valid @RequestBody UserCreateRequest request, HttpSession session) {
        emailVerificationService.checkEmailVerified(request.getUserEmail(), session);
        userService.createUser(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_REGISTER_SUCCESS);
    }

    @PostMapping(value = "/api/v1/users/oauth2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createOAuth2User(@Valid @RequestBody UserNicknameRequest request,
                                                                       HttpServletRequest httpServletRequest) {

        OAuth2User oAuth2User = SecurityUtil.getCurrentOAuth2User();
        if (oAuth2User == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        userService.createOAuth2User(request, httpServletRequest, oAuth2User);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_REGISTER_SUCCESS);
    }

    @PatchMapping(value = "/api/v1/users/{userNo}/dormancy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deactivateUser(@PathVariable Integer userNo) {
        final Integer currentUserNo = SecurityUtil.getCurrentUserNo();
        if (!Objects.equals(userNo, currentUserNo)) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        userService.deactivateUser(userNo);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_DORMANT_SUCCESS);
    }

    @PatchMapping(value = "/api/v1/users/reactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> reactivateUser(@RequestBody UserReactivateRequest  request) {
        userService.reactivateUser(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_REACTIVATE_SUCCESS);
    }

    @PatchMapping(value = "/api/v1/users/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> changePassword(@Valid @RequestBody UserPasswordUpdateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.UNAUTHORIZED_ERROR);
        }
        userService.changePassword(userNo, request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_PASSWORD_UPDATE_SUCCESS);
    }

    @PatchMapping(value = "/api/v1/users/{userNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateUser(@PathVariable Integer userNo, @Valid UserUpdateRequest request) {
        final Integer currentUserNo = SecurityUtil.getCurrentUserNo();
        if (!Objects.equals(userNo, currentUserNo)) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        userService.updateUser(userNo, request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_UPDATE_SUCCESS);
    }
}
