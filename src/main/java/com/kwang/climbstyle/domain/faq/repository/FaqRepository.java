package com.kwang.climbstyle.domain.faq.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminFaqListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaqRepository {

    Integer selectAdminFaqListCountByRequest(PaginationRequest paginationRequest);

    List<AdminFaqListResponse> selectAdminFaqList(PaginationRequest paginationRequest);
}
