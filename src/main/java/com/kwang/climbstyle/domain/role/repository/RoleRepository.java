package com.kwang.climbstyle.domain.role.repository;

import com.kwang.climbstyle.domain.role.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleRepository {

    RoleEntity selectRoleByRoleName(String roleName);
}
