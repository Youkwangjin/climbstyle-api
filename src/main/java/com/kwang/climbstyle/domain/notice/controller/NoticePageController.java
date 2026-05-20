package com.kwang.climbstyle.domain.notice.controller;

import com.kwang.climbstyle.domain.notice.dto.request.NoticeDetailRequest;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeListRequest;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeDetailResponse;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeListResponse;
import com.kwang.climbstyle.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 공지사항 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class NoticePageController {

    private final NoticeService noticeService;

    /**
     * 공지사항 목록 화면
     * [ClimbStyle] > [공지사항]
     */
    @GetMapping(value = "/notices")
    public String noticeList(NoticeListRequest request, Model model) {
        List<NoticeListResponse> responses = noticeService.getNoticeList(request);

        model.addAttribute("noticeList", responses);
        model.addAttribute("request", request);

        return "notice/list";
    }

    /**
     * 공지사항 상세 화면
     * [ClimbStyle] > [공지사항] > 상세
     */
    @GetMapping(value = "/notices/{noticeNo}")
    public String noticeDetail(@PathVariable("noticeNo") Integer noticeNo, NoticeDetailRequest request, Model model) {
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeNo);

        model.addAttribute("noticeDetail", response);
        model.addAttribute("request", request);

        return "notice/detail";
    }
}
