package com.kwang.climbstyle.domain.index.controller;

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

    @GetMapping("/")
    public String index(Model model) {
        List<RankingListResponse> rankingListResponse = rankingService.getIndexRealtimeRankingList();

        model.addAttribute("realtimeTop3", rankingListResponse);
        return "index";
    }
}
