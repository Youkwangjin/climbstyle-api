package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.domain.admin.dto.response.AdminNoticeListResponse;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeDetailRequest;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeListRequest;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeDetailResponse;
import com.kwang.climbstyle.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminNoticePageController {

    private final NoticeService noticeService;

    @GetMapping(value = "/admin/notices")
    public String adminNoticeList(NoticeListRequest request, Model model) {
        List<AdminNoticeListResponse> responses = noticeService.getAdminNoticeList(request);

        model.addAttribute("noticeList", responses);
        model.addAttribute("request", request);
        model.addAttribute("currentUrl", "/admin/notices");

        return "admin/notice/list";
    }

    @GetMapping(value = "/admin/notices/{noticeNo}")
    public String noticeDetail(@PathVariable("noticeNo") Integer noticeNo, NoticeDetailRequest request, Model model) {
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeNo);

        model.addAttribute("noticeDetail", response);
        model.addAttribute("request", request);

        return "/admin/notice/detail";
    }

    @GetMapping(value = "/admin/notices/new")
    public String newNotice() {
        return "admin/notice/new";
    }

    @GetMapping(value = "/admin/notices/{noticeNo}/edit")
    public String editNotice(@PathVariable("noticeNo") Integer noticeNo, Model model) {
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeNo);

        model.addAttribute("noticeDetail", response);

        return "admin/notice/edit";
    }
}
