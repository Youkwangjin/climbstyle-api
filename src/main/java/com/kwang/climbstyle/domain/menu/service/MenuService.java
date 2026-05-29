package com.kwang.climbstyle.domain.menu.service;

import com.kwang.climbstyle.code.menu.MenuErrorCode;
import com.kwang.climbstyle.code.role.RoleErrorCode;
import com.kwang.climbstyle.domain.menu.dto.request.MenuCreateRequest;
import com.kwang.climbstyle.domain.menu.dto.request.MenuUpdateRequest;
import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuListResponse;
import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuManagementResponse;
import com.kwang.climbstyle.domain.menu.dto.response.UserMenuListResponse;
import com.kwang.climbstyle.domain.menu.entity.MenuEntity;
import com.kwang.climbstyle.domain.menu.repository.MenuRepository;
import com.kwang.climbstyle.domain.role.entity.RoleEntity;
import com.kwang.climbstyle.domain.role.repository.RoleRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 메뉴 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-29
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final RoleRepository roleRepository;

    /**
     * 사용자 메뉴 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserMenuListResponse> getUserMenuList(Integer userNo) {
        return menuRepository.selectMenuByUserNo(userNo);
    }

    /**
     * 관리자 메뉴 목록 조회 (계층 구조)
     */
    @Transactional(readOnly = true)
    public List<AdminMenuListResponse> getAdminMenuList(Integer adminNo) {
        List<AdminMenuListResponse> flatList = menuRepository.selectMenuByAdminNo(adminNo);

        Map<Integer, AdminMenuListResponse> map = new LinkedHashMap<>();
        List<AdminMenuListResponse> roots = new ArrayList<>();

        for (AdminMenuListResponse menu : flatList) {
            map.put(menu.getMenuNo(), menu);
        }

        for (AdminMenuListResponse menu : flatList) {
            if (menu.getMenuParentNo() == null) {
                roots.add(menu);
            } else {
                AdminMenuListResponse parnet = map.get(menu.getMenuParentNo());
                if (parnet != null) {
                    parnet.getChildren().add(menu);
                }
            }
        }

        return roots;
    }

    /**
     * 전체 메뉴 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminMenuManagementResponse> getAllMenus() {
        return menuRepository.selectAllMenus();
    }

    /**
     * 최상위 메뉴 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminMenuManagementResponse> getParentMenus() {
        return menuRepository.selectParentMenus();
    }

    /**
     * 메뉴 상세 조회
     */
    @Transactional(readOnly = true)
    public AdminMenuManagementResponse getMenuDetail(Integer menuNo) {
        AdminMenuManagementResponse menu = menuRepository.selectMenuByNo(menuNo);
        if (menu == null) {
            throw new ClimbStyleException(MenuErrorCode.MENU_NOT_FOUND);
        }

        return menu;
    }

    /**
     * 메뉴 등록
     */
    @Transactional
    public void createMenu(MenuCreateRequest request) {
        final String roleName = request.getRoleName();
        final String menuCode = request.getMenuCode();

        RoleEntity roleEntity = roleRepository.selectRoleByRoleName(roleName);
        if (roleEntity == null) {
            throw new ClimbStyleException(RoleErrorCode.ROLE_NOT_FOUND);
        }

        MenuEntity entity = menuRepository.selectMenuByCode(menuCode);
        if (entity != null) {
            throw new ClimbStyleException(MenuErrorCode.MENU_CODE_DUPLICATE);
        }

        final String menuName = request.getMenuName();
        final String menuUrl = request.getMenuUrl();
        final Integer menuParentNo = request.getMenuParentNo();
        final Integer menuLevel = request.getMenuLevel();
        final Integer menuSortOrder = request.getMenuSortOrder();
        final String menuIcon = request.getMenuIcon();
        final String menuUseYn = request.getMenuUseYn();
        final LocalDateTime menuCreated = LocalDateTime.now();

        MenuEntity menuEntity = MenuEntity.builder()
                .menuCode(menuCode)
                .menuName(menuName)
                .menuUrl(menuUrl)
                .menuParentNo(menuParentNo)
                .menuLevel(menuLevel)
                .menuSortOrder(menuSortOrder)
                .menuIcon(menuIcon)
                .menuUseYn(menuUseYn)
                .menuCreated(menuCreated)
                .build();

        menuRepository.insert(menuEntity);

        final Integer roleNo = roleEntity.getRoleNo();
        final Integer menuNo = menuEntity.getMenuNo();
        roleRepository.insertRoleMenu(menuNo, roleNo);
    }

    /**
     * 메뉴 수정
     */
    @Transactional
    public void updateMenu(Integer menuNo, MenuUpdateRequest request) {
        AdminMenuManagementResponse existing = menuRepository.selectMenuByNo(menuNo);
        if (existing == null) {
            throw new ClimbStyleException(MenuErrorCode.MENU_NOT_FOUND);
        }

        final String menuCode = request.getMenuCode();

        if (!existing.getMenuCode().equals(menuCode)) {
            MenuEntity duplicate = menuRepository.selectMenuByCode(menuCode);
            if (duplicate != null) {
                throw new ClimbStyleException(MenuErrorCode.MENU_CODE_DUPLICATE);
            }
        }

        final String menuName = request.getMenuName();
        final String menuUrl = request.getMenuUrl();
        final Integer menuParentNo = request.getMenuParentNo();
        final Integer menuLevel = request.getMenuLevel();
        final Integer menuSortOrder = request.getMenuSortOrder();
        final String menuIcon = request.getMenuIcon();
        final String menuUseYn = request.getMenuUseYn();
        final LocalDateTime menuUpdated = LocalDateTime.now();

        MenuEntity menuEntity = MenuEntity.builder()
                .menuNo(menuNo)
                .menuCode(menuCode)
                .menuName(menuName)
                .menuUrl(menuUrl)
                .menuParentNo(menuParentNo)
                .menuLevel(menuLevel)
                .menuSortOrder(menuSortOrder)
                .menuIcon(menuIcon)
                .menuUseYn(menuUseYn)
                .menuUpdated(menuUpdated)
                .build();

        menuRepository.update(menuEntity);
    }

    /**
     * 메뉴 삭제
     */
    @Transactional
    public void deleteMenu(Integer menuNo) {
        AdminMenuManagementResponse existing = menuRepository.selectMenuByNo(menuNo);
        if (existing == null) {
            throw new ClimbStyleException(MenuErrorCode.MENU_NOT_FOUND);
        }

        roleRepository.deleteRoleMenuByMenuNo(menuNo);
        menuRepository.delete(menuNo);
    }
}
