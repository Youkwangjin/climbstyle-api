package com.kwang.climbstyle.domain.menu.service;

import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuListResponse;
import com.kwang.climbstyle.domain.menu.dto.response.UserMenuListResponse;
import com.kwang.climbstyle.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<UserMenuListResponse> getUserMenuList(Integer userNo) {
        return menuRepository.selectMenuByUserNo(userNo);
    }

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
}
