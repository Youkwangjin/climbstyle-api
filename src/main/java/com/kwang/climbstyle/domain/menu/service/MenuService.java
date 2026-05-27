package com.kwang.climbstyle.domain.menu.service;

import com.kwang.climbstyle.code.menu.MenuErrorCode;
import com.kwang.climbstyle.code.role.RoleErrorCode;
import com.kwang.climbstyle.domain.menu.dto.request.MenuCreateRequest;
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


@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserMenuListResponse> getUserMenuList(Integer userNo) {
        return menuRepository.selectMenuByUserNo(userNo);
    }

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

    @Transactional(readOnly = true)
    public List<AdminMenuManagementResponse> getAllMenus() {
        return menuRepository.selectAllMenus();
    }

    @Transactional(readOnly = true)
    public List<AdminMenuManagementResponse> getParentMenus() {
        return menuRepository.selectParentMenus();
    }

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
        menuRepository.insertRoleMenu(menuNo, roleNo);
    }
}
