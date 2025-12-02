package com.kwang.climbstyle.domain.jwt.repository;

import com.kwang.climbstyle.domain.jwt.entity.JwtRefreshEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JwtRepository {

    Boolean existsByJwtRefreshToken(@Param("jwtRefreshToken") String jwtRefreshToken);

    void insert(JwtRefreshEntity jwtRefreshEntity);

    void revoke(@Param("userNo") Integer userNo);
}
