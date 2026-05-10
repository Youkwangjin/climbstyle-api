package com.kwang.climbstyle.domain.user.controller;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.code.user.UserSuccessCode;
import com.kwang.climbstyle.code.user.VerificationPurpose;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.user.dto.request.UserCreateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailVerificationRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserIdRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserNicknameRequest;
import com.kwang.climbstyle.domain.user.service.EmailVerificationService;
import com.kwang.climbstyle.domain.user.service.UserRegistrationService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 회원가입 API 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@RestController
@RequiredArgsConstructor
public class UserRegistrationApiController {

    private final UserRegistrationService userRegistrationService;

    private final EmailVerificationService emailVerificationService;

    /**
     * 아이디 사용 가능 여부 확인
     * [ClimbStyle] > [회원가입] > 아이디 중복 확인
     */
    @PostMapping(value = "/api/v1/users/id/availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> checkUserIdDuplicate(@RequestBody @Valid UserIdRequest request) {
        userRegistrationService.checkUserIdDuplicate(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_ID_AVAILABLE);
    }

    /**
     * 이메일 인증 코드 발송
     * [ClimbStyle] > [회원가입] > [이메일 인증] > 인증 코드 발송
     */
    @PostMapping(value = "/api/v1/users/email/verification-code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> sendEmailVerificationCode(@RequestBody @Valid UserEmailRequest request, HttpSession session) {
        emailVerificationService.sendCode(VerificationPurpose.REGISTER, request, session);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_EMAIL_VERIFICATION_CODE_SENT);
    }

    /**
     * 이메일 인증 코드 검증
     * [ClimbStyle] > [회원가입] > [이메일 인증] > 인증 코드 검증
     */
    @PostMapping(value = "/api/v1/users/email/verification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> verifyEmailCode(@RequestBody @Valid UserEmailVerificationRequest request, HttpSession session) {
        emailVerificationService.verifyCode(VerificationPurpose.REGISTER, request, session);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_EMAIL_VERIFIED);
    }

    /**
     * 닉네임 사용 가능 여부 확인
     * [ClimbStyle] > [회원가입] > 닉네임 중복 확인
     */
    @PostMapping(value = "/api/v1/users/nickname/availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> checkUserNickNameDuplicate(@RequestBody @Valid UserNicknameRequest request) {
        userRegistrationService.checkUserNicknameDuplicate(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_NICKNAME_AVAILABLE);
    }

    /**
     * 일반 회원 가입
     * [ClimbStyle] > [회원가입] > 일반 회원 등록
     */
    @PostMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createUser(@RequestBody @Valid UserCreateRequest request, HttpSession session) {
        emailVerificationService.checkVerified(VerificationPurpose.REGISTER, request.getUserEmail(), session);
        userRegistrationService.createUser(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_REGISTER_SUCCESS);
    }

    /**
     * 소셜 회원 가입
     * [ClimbStyle] > [회원가입] > 소셜 회원 등록
     */
    @PostMapping(value = "/api/v1/users/oauth2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createOAuth2User(@RequestBody @Valid UserNicknameRequest request,
                                                                       HttpServletRequest httpServletRequest) {

        OAuth2User oAuth2User = SecurityUtil.getCurrentOAuth2User();
        if (oAuth2User == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        userRegistrationService.createOAuth2User(request, httpServletRequest, oAuth2User);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_REGISTER_SUCCESS);
    }
}
