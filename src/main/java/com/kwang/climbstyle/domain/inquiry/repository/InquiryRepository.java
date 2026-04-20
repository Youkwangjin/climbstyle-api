package com.kwang.climbstyle.domain.inquiry.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminInquiryListResponse;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryDetailResponse;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryListResponse;
import com.kwang.climbstyle.domain.inquiry.entity.InquiryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryRepository {

    Integer selectInquiryListCountByRequest(@Param("request") PaginationRequest request, @Param("userNo") Integer userNo);

    List<InquiryListResponse> selectUserInquiryList(@Param("request") PaginationRequest request, @Param("userNo") Integer userNo);

    List<AdminInquiryListResponse> selectPendingInquiryList();

    InquiryDetailResponse selectInquiryByNo(@Param("inquiryNo") Integer inquiryNo, @Param("userNo") Integer userNo);

    void insert(InquiryEntity inquiryEntity);

    void update(InquiryEntity inquiryEntity);

    void delete(InquiryEntity inquiryEntity);
}
