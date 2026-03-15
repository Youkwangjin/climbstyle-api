package com.kwang.climbstyle.domain.role.repository;

import com.kwang.climbstyle.domain.role.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleRepository {

    void insert(UserRoleEntity userRoleEntity);
}
