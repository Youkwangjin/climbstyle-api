package com.kwang.climbstyle.domain.menu.repository;

import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuListResponse;
import com.kwang.climbstyle.domain.menu.entity.MenuEntity;
import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuManagementResponse;
import com.kwang.climbstyle.domain.menu.dto.response.UserMenuListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuRepository {

    List<UserMenuListResponse> selectMenuByUserNo(@Param("userNo") Integer userNo);

    List<AdminMenuListResponse> selectMenuByAdminNo(@Param("adminNo") Integer adminNo);

    List<AdminMenuManagementResponse> selectAllMenus();

    List<AdminMenuManagementResponse> selectParentMenus();

    AdminMenuManagementResponse selectMenuByNo(@Param("menuNo") Integer menuNo);

    MenuEntity selectMenuByCode(@Param("menuCode") String menuCode);

    void insert(MenuEntity menuEntity);

    void insertRoleMenu(@Param("menuNo") Integer menuNo, @Param("roleNo") Integer roleNo);

    void update(MenuEntity menuEntity);
}
