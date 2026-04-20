package com.kwang.climbstyle.domain.inquiry.repository;

import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryFileResponse;
import com.kwang.climbstyle.domain.inquiry.entity.InquiryFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryFileRepository {

    InquiryFileResponse selectInquiryFileByNo(@Param("inquiryFileNo") Integer inquiryFileNo);

    List<InquiryFileResponse> selectInquiryFileByInquiryNo(@Param("inquiryNo") Integer inquiryNo);

    void insert(InquiryFileEntity nquiryFileEntity);

    void delete(@Param("inquiryFileNo") Integer inquiryFileNo);
}
