package com.kwang.climbstyle.domain.inquiry.repository;

import com.kwang.climbstyle.domain.inquiry.entity.InquiryFileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryFileRepository {

    void insert(InquiryFileEntity nquiryFileEntity);
}
