package com.kwang.climbstyle.domain.feed.controller;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.feed.dto.response.FeedListResponse;
import com.kwang.climbstyle.domain.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedPageController {

    private final FeedService feedService;

    @GetMapping(value = "/feed")
    public String myFeed(CommonListRequest request, Model model) {
        List<FeedListResponse> feedList = feedService.getFeedList(request);
        model.addAttribute("feedList", feedList);
        model.addAttribute("request", request);
        return "feed/feed";
    }

    @GetMapping(value = "/feed/new")
    public String newFeed() {
        return "feed/new";
    }
}
