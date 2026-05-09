package com.kwang.climbstyle.domain.user.controller;

import com.kwang.climbstyle.code.user.UserSuccessCode;
import com.kwang.climbstyle.code.user.VerificationPurpose;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailVerificationRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserFindIdRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserFindPwRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserResetPasswordRequest;
import com.kwang.climbstyle.domain.user.service.EmailVerificationService;
import com.kwang.climbstyle.domain.user.service.UserAccountRecoveryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 아이디·비밀번호 찾기 API 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@RestController
@RequiredArgsConstructor
public class UserAccountRecoveryApiController {

    private final UserAccountRecoveryService userAccountRecoveryService;

    private final EmailVerificationService emailVerificationService;

    /**
     * 아이디 찾기 이메일 인증 코드 발송
     * [ClimbStyle] > [아이디 찾기] > [이메일 인증] > 인증 코드 발송
     */
    @PostMapping(value = "/api/v1/users/id/find/verification-code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> sendIdFindVerificationCode(@RequestBody @Valid UserEmailRequest request, HttpSession session) {
        emailVerificationService.sendCode(VerificationPurpose.FIND_ID, request, session);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_EMAIL_VERIFICATION_CODE_SENT);
    }

    /**
     * 아이디 찾기
     * [ClimbStyle] > [아이디 찾기] > 아이디 조회
     */
    @PostMapping(value = "/api/v1/users/id/find", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> findUserId(@RequestBody @Valid UserFindIdRequest request) {
        String maskedId = userAccountRecoveryService.findUserId(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_FIND_ID_SUCCESS, maskedId);
    }

    /**
     * 아이디 공개 조회
     * [ClimbStyle] > [아이디 찾기] > 아이디 공개
     */
    @PostMapping(value = "/api/v1/users/id/reveal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> revealUserId(@RequestBody @Valid UserEmailVerificationRequest request, HttpSession session) {
        emailVerificationService.verifyCode(VerificationPurpose.FIND_ID, request, session);
        final String userId = userAccountRecoveryService.getUserIdByEmail(request.getUserEmail());

        return ApiResponseBuilder.ok(UserSuccessCode.USER_FIND_ID_REVEAL_SUCCESS, userId);
    }

    /**
     * 비밀번호 찾기 이메일 인증 코드 발송
     * [ClimbStyle] > [비밀번호 찾기] > [이메일 인증] > 인증 코드 발송
     */
    @PostMapping(value = "/api/v1/users/password/recovery/verification-code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> sendPwFindVerificationCode(@RequestBody @Valid UserFindPwRequest request, HttpSession session) {
        userAccountRecoveryService.validateUserForPwReset(request);
        emailVerificationService.sendCode(VerificationPurpose.FIND_PW, request.getUserEmail(), session);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_FIND_PW_CODE_SENT);
    }

    /**
     * 비밀번호 찾기 이메일 인증 코드 검증
     * [ClimbStyle] > [비밀번호 찾기] > [이메일 인증] > 인증 코드 검증
     */
    @PostMapping(value = "/api/v1/users/password/recovery/verification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> verifyPwRecoveryCode(@RequestBody @Valid UserEmailVerificationRequest request, HttpSession session) {
        emailVerificationService.verifyCode(VerificationPurpose.FIND_PW, request, session);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_EMAIL_VERIFIED);
    }

    /**
     * 비밀번호 재설정
     * [ClimbStyle] > [비밀번호 찾기] > 비밀번호 재설정
     */
    @PostMapping(value = "/api/v1/users/password/recovery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> resetPassword(@RequestBody @Valid UserResetPasswordRequest request, HttpSession session) {
        emailVerificationService.checkVerified(VerificationPurpose.FIND_PW, request.getUserEmail(), session);
        userAccountRecoveryService.resetPassword(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_RESET_PASSWORD_SUCCESS);
    }
}
