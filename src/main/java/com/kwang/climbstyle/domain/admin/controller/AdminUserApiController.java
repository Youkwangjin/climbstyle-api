package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.user.UserSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.admin.dto.request.AdminUserSuspendRequest;
import com.kwang.climbstyle.domain.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 관리자 사용자 API 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-17
 * @version : 1.0
 */
@RestController
@RequiredArgsConstructor
public class AdminUserApiController {

    private final AdminUserService adminUserService;

    /**
     * 사용자 정지 처리
     */
    @PatchMapping(value = "/api/v1/admin/users/{userNo}/suspend", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> suspendUser(@PathVariable Integer userNo,
                                                                  @RequestBody AdminUserSuspendRequest request) {
        adminUserService.suspendUser(userNo, request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_SUSPEND_SUCCESS);
    }

    /**
     * 사용자 정지 취소 처리
     */
    @PatchMapping(value = "/api/v1/admin/users/{userNo}/unsuspend", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> unsuspendUser(@PathVariable Integer userNo) {
        adminUserService.unsuspendUser(userNo);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_UNSUSPEND_SUCCESS);
    }
}
