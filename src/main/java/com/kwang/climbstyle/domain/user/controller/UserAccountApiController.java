package com.kwang.climbstyle.domain.user.controller;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.code.user.UserSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.user.dto.request.UserPasswordUpdateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserReactivateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserUpdateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserWithdrawRequest;
import com.kwang.climbstyle.domain.user.service.UserAccountService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 사용자 계정 관리 API 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-09
 * @version : 1.0
 */
@RestController
@RequiredArgsConstructor
public class UserAccountApiController {

    private final UserAccountService userAccountService;

    /**
     * 회원 정보 수정
     * [ClimbStyle] > [마이페이지] > 회원 정보 수정
     */
    @PatchMapping(value = "/api/v1/users/{userNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateUser(@PathVariable Integer userNo, @Valid UserUpdateRequest request) {
        final Integer currentUserNo = SecurityUtil.getCurrentUserNo();
        if (!Objects.equals(userNo, currentUserNo)) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        userAccountService.updateUser(userNo, request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_UPDATE_SUCCESS);
    }

    /**
     * 비밀번호 변경
     * [ClimbStyle] > [마이페이지] > 비밀번호 변경
     */
    @PatchMapping(value = "/api/v1/users/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> changePassword(@Valid @RequestBody UserPasswordUpdateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.UNAUTHORIZED_ERROR);
        }
        userAccountService.changePassword(userNo, request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_PASSWORD_UPDATE_SUCCESS);
    }

    /**
     * 계정 휴면 처리
     * [ClimbStyle] > [마이페이지] > 계정 휴면
     */
    @PatchMapping(value = "/api/v1/users/{userNo}/dormancy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deactivateUser(@PathVariable Integer userNo) {
        final Integer currentUserNo = SecurityUtil.getCurrentUserNo();
        if (!Objects.equals(userNo, currentUserNo)) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        userAccountService.deactivateUser(userNo);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_DORMANT_SUCCESS);
    }

    /**
     * 계정 휴면 해제
     * [ClimbStyle] > [휴면 해제] > 휴면 계정 복구
     */
    @PatchMapping(value = "/api/v1/users/reactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> reactivateUser(@RequestBody @Valid UserReactivateRequest request) {
        userAccountService.reactivateUser(request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_REACTIVATE_SUCCESS);
    }

    /**
     * 회원 탈퇴
     * [ClimbStyle] > [마이페이지] > 회원 탈퇴
     */
    @DeleteMapping(value = "/api/v1/users/{userNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> withdrawUser(@PathVariable Integer userNo,
                                                                   @RequestBody UserWithdrawRequest request) {
        final Integer currentUserNo = SecurityUtil.getCurrentUserNo();
        if (!Objects.equals(userNo, currentUserNo)) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        userAccountService.withdrawUser(userNo, request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_DELETE_SUCCESS);
    }
}
