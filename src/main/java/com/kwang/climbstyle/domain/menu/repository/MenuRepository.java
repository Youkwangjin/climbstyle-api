package com.kwang.climbstyle.domain.menu.repository;

import com.kwang.climbstyle.domain.menu.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuRepository {
    List<MenuEntity> selectMenuByUserRole(String roleName);
}
