package com.kwang.climbstyle.domain.role.repository;

import com.kwang.climbstyle.domain.role.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleRepository {

    void insert(UserRoleEntity userRoleEntity);

    void deleteByUserNo(@Param("userNo") Integer userNo);
}
