package com.kwang.climbstyle.domain.user.repository;

import com.kwang.climbstyle.domain.admin.dto.response.AdminUserListResponse;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository {

    Boolean existUserId(@Param("userId") String userId);

    Boolean existUserEmail(@Param("userEmail") String userEmail);

    Boolean existUserNickname(@Param("userNickname") String userNickname);

    UserEntity selectUserByNo(@Param("userNo") Integer userNo);

    UserEntity selectUserById(@Param("userId") String userId);

    UserEntity selectUserByOAuthId(@Param("userOauthProvider") String userOauthProvider,
                                   @Param("userOauthId") String userOauthId);

    List<AdminUserListResponse> selectRecentUserList();

    void insert(UserEntity userEntity);

    void deactivateUser(UserEntity userEntity);

    void reactivateUser(UserEntity userEntity);

    void update(UserEntity userEntity);

    void updatePassword(UserEntity userEntity);
}
