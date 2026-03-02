package com.kwang.climbstyle.domain.ranking.controller;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import com.kwang.climbstyle.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RankingPageController {

    private final RankingService rankingService;

    @GetMapping(value = "/rankings/realtime")
    public String realtime(CommonListRequest request, Model model) {
        List<RankingListResponse> responses = rankingService.getRealtimeRankingList(request);
        model.addAttribute("rankingList", responses);
        model.addAttribute("request", request);
        model.addAttribute("currentUrl", "/rankings/realtime");

        return "ranking/realtime";
    }

    @GetMapping(value = "/rankings/weekly")
    public String weekly(CommonListRequest request, Model model) {
        List<RankingListResponse> responses = rankingService.getWeeklyRankingList(request);
        model.addAttribute("rankingList", responses);
        model.addAttribute("request", request);
        model.addAttribute("currentUrl", "/rankings/weekly");

        return "ranking/weekly";
    }
}
