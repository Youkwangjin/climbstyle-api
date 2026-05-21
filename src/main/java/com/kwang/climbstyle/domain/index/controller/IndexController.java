package com.kwang.climbstyle.domain.index.controller;

import com.kwang.climbstyle.domain.banner.dto.response.BannerDetailResponse;
import com.kwang.climbstyle.domain.banner.service.BannerService;
import com.kwang.climbstyle.domain.feed.dto.response.FeedListResponse;
import com.kwang.climbstyle.domain.feed.service.FeedService;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import com.kwang.climbstyle.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 홈 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RankingService rankingService;

    private final BannerService bannerService;

    private final FeedService feedService;

    /**
     * 홈 화면
     * [ClimbStyle] > 홈
     */
    @GetMapping(value = "/")
    public String index(Model model) {
        List<RankingListResponse> rankingListResponse = rankingService.getIndexRealtimeRankingList();
        List<BannerDetailResponse> bannerList = bannerService.getVisibleBanners();
        List<FeedListResponse> latestFeeds = feedService.getLatestFeeds(6);

        model.addAttribute("realtimeTop3", rankingListResponse);
        model.addAttribute("bannerList", bannerList);
        model.addAttribute("latestFeeds", latestFeeds);

        return "index";
    }

    /**
     * 서비스 소개 화면
     * [ClimbStyle] > 서비스 소개
     */
    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

    /**
     * 이용약관 화면
     * [ClimbStyle] > 이용약관
     */
    @GetMapping(value = "/terms")
    public String terms() {
        return "policy/terms";
    }

    /**
     * 개인정보처리방침 화면
     * [ClimbStyle] > 개인정보처리방침
     */
    @GetMapping(value = "/privacy")
    public String privacy() {
        return "policy/privacy";
    }
}
