package com.kwang.climbstyle.domain.notice.repository;

import com.kwang.climbstyle.domain.notice.dto.response.NoticeFileResponse;
import com.kwang.climbstyle.domain.notice.entity.NoticeFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeFileRepository {

    NoticeFileResponse selectNoticeFileByNo(@Param("noticeFileNo") Integer noticeFileNo);

    List<NoticeFileResponse> selectNoticeFileByNoticeNo(@Param("noticeNo") Integer noticeNo);

    void insert(NoticeFileEntity noticeFileEntity);

    void delete(@Param("noticeFileNo") Integer noticeFileNo);
}
