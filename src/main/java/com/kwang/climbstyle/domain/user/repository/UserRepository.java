package com.kwang.climbstyle.domain.user.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    Boolean existUserId(String userId);

    Boolean existUserEmail(String userEmail);

    Boolean existUserNickName(String userNickName);
}
