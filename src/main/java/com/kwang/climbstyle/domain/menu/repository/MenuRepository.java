package com.kwang.climbstyle.domain.menu.repository;

import com.kwang.climbstyle.domain.menu.dto.response.AdminMenuListResponse;
import com.kwang.climbstyle.domain.menu.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuRepository {

    List<MenuEntity> selectMenuByUserRole(@Param("roleName") String roleName);

    List<AdminMenuListResponse> selectMenuByAdminNo(@Param("adminNo") Integer adminNo);
}
