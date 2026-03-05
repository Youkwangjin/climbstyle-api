package com.kwang.climbstyle.domain.notice.controller;

import com.kwang.climbstyle.domain.notice.dto.request.NoticeListRequest;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeListResponse;
import com.kwang.climbstyle.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoticePageController {

    private final NoticeService noticeService;

    @GetMapping(value = "/notice/list")
    public String noticeList(NoticeListRequest request, Model model) {
        List<NoticeListResponse> responses = noticeService.getNoticeList(request);

        model.addAttribute("noticeList", responses);
        model.addAttribute("request", request);
        model.addAttribute("currentUrl", "/notice/list");

        return "notice/list";
    }
}
