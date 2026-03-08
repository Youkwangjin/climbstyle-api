package com.kwang.climbstyle.domain.notice.service;

import com.kwang.climbstyle.domain.notice.dto.request.NoticeListRequest;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeListResponse;
import com.kwang.climbstyle.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeListResponse> getNoticeList(NoticeListRequest request) {
        request.setTotalCount(noticeRepository.selectNoticeListCountByRequest(request));

        List<NoticeListResponse> noticeList = noticeRepository.selectNoticeList(request);

        LocalDateTime sevenDaysAgo =  LocalDateTime.now().minusDays(7);
        for (NoticeListResponse notice : noticeList) {
            notice.setNoticeNew(notice.getNoticeCreated().isAfter(sevenDaysAgo));
        }

        return noticeList;
    }
}
