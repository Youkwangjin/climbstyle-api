package com.kwang.climbstyle.domain.ranking.controller;

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

    @GetMapping(value = "/ranking")
    public String ranking(Model model) {
        List<RankingListResponse> response = rankingService.getRankingList();
        model.addAttribute("rankingList", response);

        return "ranking/ranking";
    }
}
