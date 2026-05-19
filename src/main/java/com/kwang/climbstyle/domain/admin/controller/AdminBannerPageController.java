package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.domain.admin.dto.response.AdminBannerListResponse;
import com.kwang.climbstyle.domain.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminBannerPageController {

    private final BannerService bannerService;

    @GetMapping(value = "/admin/banners")
    public String adminBannerList(Model model) {
        List<AdminBannerListResponse> bannerList = bannerService.getAdminBannerList();

        model.addAttribute("bannerList", bannerList);

        return "admin/banner/list";
    }

    @GetMapping(value = "/admin/banners/new")
    public String adminBannerNew() {
        return "admin/banner/new";
    }
}
