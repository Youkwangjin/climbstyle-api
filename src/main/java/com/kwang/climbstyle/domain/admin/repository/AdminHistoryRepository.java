package com.kwang.climbstyle.domain.admin.repository;

import com.kwang.climbstyle.domain.admin.entity.AdminLoginHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminHistoryRepository {

    void insert (AdminLoginHistoryEntity adminLoginHistoryEntity);
}
