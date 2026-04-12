package com.kwang.climbstyle.domain.admin.repository;

import com.kwang.climbstyle.domain.admin.dto.response.AdminDashboardStatResponse;
import com.kwang.climbstyle.domain.admin.entity.AdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminRepository {

    AdminDashboardStatResponse selectDashboardStat();

    AdminEntity selectAdminById(@Param("adminId") String adminId);
}
