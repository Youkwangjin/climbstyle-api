package com.kwang.climbstyle.domain.notice.repository;

import com.kwang.climbstyle.domain.notice.entity.NoticeFileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeFileRepository {

    void insert(NoticeFileEntity noticeFileEntity);
}
