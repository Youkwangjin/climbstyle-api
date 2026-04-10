package com.kwang.climbstyle.domain.notice.repository;

import com.kwang.climbstyle.common.protocal.PaginationRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminNoticeListResponse;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeListResponse;
import com.kwang.climbstyle.domain.notice.entity.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeRepository {

    Integer selectNoticeListCountByRequest(PaginationRequest paginationRequest);

    Integer selectAdminNoticeListCountByRequest(PaginationRequest paginationRequest);

    List<NoticeListResponse> selectNoticeList(PaginationRequest paginationRequest);

    List<AdminNoticeListResponse> selectAdminNoticeList(PaginationRequest paginationRequest);

    List<AdminNoticeListResponse> selectRecentNoticeList();

    void insert(NoticeEntity noticeEntity);
}
