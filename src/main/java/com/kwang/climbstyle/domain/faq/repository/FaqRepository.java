package com.kwang.climbstyle.domain.faq.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminFaqListResponse;
import com.kwang.climbstyle.domain.faq.dto.response.FaqDetailResponse;
import com.kwang.climbstyle.domain.faq.entity.FaqEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaqRepository {

    Integer selectAdminFaqListCountByRequest(PaginationRequest paginationRequest);

    List<AdminFaqListResponse> selectAdminFaqList(PaginationRequest paginationRequest);

    FaqDetailResponse selectAdminFaqByNo(@Param("faqNo") Integer faqNo);

    void insert(FaqEntity faqEntity);

    void update(FaqEntity faqEntity);

    void delete(@Param("faqNo") Integer faqNo);
}
