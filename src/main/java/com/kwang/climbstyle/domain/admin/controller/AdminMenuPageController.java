package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.menu.MenuRegisterCode;
import com.kwang.climbstyle.code.role.RoleCode;
import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuManagementResponse;
import com.kwang.climbstyle.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 관리자 메뉴 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-26
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class AdminMenuPageController {

    private final MenuService menuService;

    /**
     * 메뉴 목록 페이지
     */
    @GetMapping(value = "/admin/menus")
    public String adminMenuList(Model model) {
        List<AdminMenuManagementResponse> menuList = menuService.getAllMenus();

        model.addAttribute("menus", menuList);

        return "admin/menu/list";
    }

    /**
     * 메뉴 등록 페이지
     */
    @GetMapping(value = "/admin/menus/new")
    public String adminMenuNew(Model model) {
        List<AdminMenuManagementResponse> parentMenus = menuService.getParentMenus();

        model.addAttribute("userCodes", MenuRegisterCode.getUserCodes());
        model.addAttribute("adminCodes", MenuRegisterCode.getAdminCodes());
        model.addAttribute("parentMenus", parentMenus);
        model.addAttribute("roles", new RoleCode[]{RoleCode.ROLE_USER, RoleCode.ROLE_ADMIN});

        return "admin/menu/new";
    }
}
