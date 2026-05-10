package com.kwang.climbstyle.domain.feed.controller;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.feed.dto.response.FeedListResponse;
import com.kwang.climbstyle.domain.feed.service.FeedService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedPageController {

    private final FeedService feedService;

    @GetMapping(value = "/feeds")
    public String feed(CommonListRequest request, Model model) {
        List<FeedListResponse> feedList = feedService.getFeedList(request);
        model.addAttribute("feedList", feedList);
        model.addAttribute("request", request);
        return "feed/feed";
    }

    @GetMapping(value = "/my/feed")
    public String myFeed(CommonListRequest request, Model model) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }
        List<FeedListResponse> feedList = feedService.getMyFeedList(request, userNo);
        model.addAttribute("feedList", feedList);
        model.addAttribute("request", request);
        return "feed/feed";
    }

    @GetMapping(value = "/feeds/new")
    public String newFeed() {
        return "feed/new";
    }
}
