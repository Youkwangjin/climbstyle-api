package com.kwang.climbstyle.domain.user.repository;

import com.kwang.climbstyle.domain.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    Boolean existUserId(@Param("userId") String userId);

    Boolean existUserEmail(@Param("userEmail") String userEmail);

    Boolean existUserNickName(@Param("userNickName") String userNickName);

    UserEntity selectUserByNo(@Param("userNo") Integer userNo);

    UserEntity selectUserById(@Param("userId") String userId);

    void insert(UserEntity userEntity);
}
