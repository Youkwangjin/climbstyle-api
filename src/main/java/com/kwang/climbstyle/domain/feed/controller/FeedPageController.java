package com.kwang.climbstyle.domain.feed.controller;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCursorRequest;
import com.kwang.climbstyle.domain.feed.dto.response.FeedCursorResponse;
import com.kwang.climbstyle.domain.feed.service.FeedService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FeedPageController {

    private final FeedService feedService;

    @GetMapping(value = "/feeds")
    public String feed(Model model) {
        FeedCursorResponse initial = feedService.getFeedListByCursor(new FeedCursorRequest());
        model.addAttribute("feedList", initial.getFeeds());
        model.addAttribute("nextCursor", initial.getNextCursor());
        model.addAttribute("hasNext", initial.isHasNext());
        model.addAttribute("feedApiUrl", "/api/v1/feeds");
        return "feed/feed";
    }

    @GetMapping(value = "/my/feed")
    public String myFeed(Model model) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }
        FeedCursorResponse initial = feedService.getMyFeedListByCursor(new FeedCursorRequest(), userNo);
        model.addAttribute("feedList", initial.getFeeds());
        model.addAttribute("nextCursor", initial.getNextCursor());
        model.addAttribute("hasNext", initial.isHasNext());
        model.addAttribute("feedApiUrl", "/api/v1/feeds/my");
        return "feed/feed";
    }

    @GetMapping(value = "/feeds/new")
    public String newFeed() {
        return "feed/new";
    }
}
