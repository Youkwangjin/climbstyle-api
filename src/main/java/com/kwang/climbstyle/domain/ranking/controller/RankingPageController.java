package com.kwang.climbstyle.domain.ranking.controller;

import com.kwang.climbstyle.code.ranking.RankingType;
import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.ranking.dto.response.RankingListResponse;
import com.kwang.climbstyle.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 랭킹 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class RankingPageController {

    private final RankingService rankingService;

    /**
     * 실시간 랭킹 화면
     * [ClimbStyle] > [랭킹] > 실시간
     */
    @GetMapping(value = "/rankings/realtime")
    public String realtime(CommonListRequest request, Model model) {
        List<RankingListResponse> responses = rankingService.getRealtimeRankingList(request);
        model.addAttribute("rankingList", responses);
        model.addAttribute("request", request);
        model.addAttribute("currentUrl", RankingType.REALTIME.getUrl());
        model.addAttribute("pageTitle", RankingType.REALTIME.getDescription());
        model.addAttribute("allRankingTypes", RankingType.values());

        return "ranking/list";
    }

    /**
     * 주간 랭킹 화면
     * [ClimbStyle] > [랭킹] > 주간
     */
    @GetMapping(value = "/rankings/weekly")
    public String weekly(CommonListRequest request, Model model) {
        List<RankingListResponse> responses = rankingService.getWeeklyRankingList(request);
        model.addAttribute("rankingList", responses);
        model.addAttribute("request", request);
        model.addAttribute("currentUrl", RankingType.WEEKLY.getUrl());
        model.addAttribute("pageTitle", RankingType.WEEKLY.getDescription());
        model.addAttribute("allRankingTypes", RankingType.values());

        return "ranking/list";
    }

    /**
     * 월간 랭킹 화면
     * [ClimbStyle] > [랭킹] > 월간
     */
    @GetMapping(value = "/rankings/monthly")
    public String monthly(CommonListRequest request, Model model) {
        List<RankingListResponse> responses = rankingService.getMonthlyRankingList(request);
        model.addAttribute("rankingList", responses);
        model.addAttribute("request", request);
        model.addAttribute("currentUrl", RankingType.MONTHLY.getUrl());
        model.addAttribute("pageTitle", RankingType.MONTHLY.getDescription());
        model.addAttribute("allRankingTypes", RankingType.values());

        return "ranking/list";
    }
}
