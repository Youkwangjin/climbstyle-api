package com.kwang.climbstyle.domain.role.repository;

import com.kwang.climbstyle.domain.role.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleRepository {

    RoleEntity selectRoleByRoleName(@Param("roleName") String roleName);
}
