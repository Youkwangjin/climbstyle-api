package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.user.UserSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.admin.dto.request.AdminUserSuspendRequest;
import com.kwang.climbstyle.domain.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminUserApiController {

    private final AdminUserService adminUserService;

    @PatchMapping(value = "/api/v1/admin/users/{userNo}/suspend", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> suspendUser(@PathVariable Integer userNo,
                                                                  @RequestBody AdminUserSuspendRequest request) {
        adminUserService.suspendUser(userNo, request);

        return ApiResponseBuilder.ok(UserSuccessCode.USER_SUSPEND_SUCCESS);
    }
}
