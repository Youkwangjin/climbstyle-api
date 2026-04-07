package com.kwang.climbstyle.domain.inquiry.repository;

import com.kwang.climbstyle.domain.admin.dto.response.AdminInquiryListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryRepository {

    List<AdminInquiryListResponse> selectPendingInquiryList();
}
