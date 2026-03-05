package com.kwang.climbstyle.domain.notice.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeRepository {

    Integer selectNoticeListCountByRequest(PaginationRequest paginationRequest);

    List<NoticeListResponse> selectNoticeList(PaginationRequest paginationRequest);
}
