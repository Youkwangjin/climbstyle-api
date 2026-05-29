package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.menu.MenuSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.menu.dto.request.MenuCreateRequest;
import com.kwang.climbstyle.domain.menu.dto.request.MenuUpdateRequest;
import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuListResponse;
import com.kwang.climbstyle.domain.menu.service.MenuService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 관리자 메뉴 API 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-26
 * @version : 1.0
 */
@RestController
@RequiredArgsConstructor
public class AdminMenuApiController {

    private final MenuService menuService;

    /**
     * 메뉴 등록
     */
    @PostMapping(value = "/api/v1/menus", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createMenu(@RequestBody @Valid MenuCreateRequest request,
                                                                 HttpSession session) {
        menuService.createMenu(request);

        List<AdminMenuListResponse> updatedMenuList = menuService.getAdminMenuList(SecurityUtil.getCurrentAdminNo());
        session.setAttribute("menuList", updatedMenuList);

        return ApiResponseBuilder.ok(MenuSuccessCode.MENU_CREATE_SUCCESS);
    }

    /**
     * 메뉴 수정
     */
    @PatchMapping(value = "/api/v1/menus/{menuNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateMenu(@PathVariable Integer menuNo,
                                                                 @RequestBody @Valid MenuUpdateRequest request,
                                                                 HttpSession session) {
        menuService.updateMenu(menuNo, request);

        List<AdminMenuListResponse> updatedMenuList = menuService.getAdminMenuList(SecurityUtil.getCurrentAdminNo());
        session.setAttribute("menuList", updatedMenuList);

        return ApiResponseBuilder.ok(MenuSuccessCode.MENU_UPDATE_SUCCESS);
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping(value = "/api/v1/menus/{menuNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deleteMenu(@PathVariable Integer menuNo,
                                                                 HttpSession session) {
        menuService.deleteMenu(menuNo);

        List<AdminMenuListResponse> updatedMenuList = menuService.getAdminMenuList(SecurityUtil.getCurrentAdminNo());
        session.setAttribute("menuList", updatedMenuList);

        return ApiResponseBuilder.ok(MenuSuccessCode.MENU_DELETE_SUCCESS);
    }
}
