package com.kwang.climbstyle.domain.jwt.repository;

import com.kwang.climbstyle.domain.jwt.entity.JwtRefreshEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JwtRepository {

    Boolean existsByJwtRefreshToken(String jwtRefreshToken);

    void insert(JwtRefreshEntity jwtRefreshEntity);

    void revoke(Integer userNo);
}
