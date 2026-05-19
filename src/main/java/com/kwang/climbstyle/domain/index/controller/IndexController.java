package com.kwang.climbstyle.domain.index.controller;

import com.kwang.climbstyle.domain.banner.dto.response.BannerDetailResponse;
import com.kwang.climbstyle.domain.banner.service.BannerService;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import com.kwang.climbstyle.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RankingService rankingService;

    private final BannerService bannerService;

    @GetMapping(value = "/")
    public String index(Model model) {
        List<RankingListResponse> rankingListResponse = rankingService.getIndexRealtimeRankingList();
        List<BannerDetailResponse> bannerList = bannerService.getVisibleBanners();

        model.addAttribute("realtimeTop3", rankingListResponse);
        model.addAttribute("bannerList", bannerList);
        return "index";
    }

    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

    @GetMapping(value = "/terms")
    public String terms() {
        return "policy/terms";
    }

    @GetMapping(value = "/privacy")
    public String privacy() {
        return "policy/privacy";
    }
}
