package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.domain.admin.dto.response.AdminBannerListResponse;
import com.kwang.climbstyle.domain.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 관리자 배너 페이지 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Controller
@RequiredArgsConstructor
public class AdminBannerPageController {

    private final BannerService bannerService;

    /**
     * 배너 목록 화면
     * [관리자] > [배너 관리]
     */
    @GetMapping(value = "/admin/banners")
    public String adminBannerList(Model model) {
        List<AdminBannerListResponse> bannerList = bannerService.getAdminBannerList();

        model.addAttribute("bannerList", bannerList);

        return "admin/banner/list";
    }

    /**
     * 배너 등록 화면
     * [관리자] > [배너 관리] > 배너 등록
     */
    @GetMapping(value = "/admin/banners/new")
    public String adminBannerNew() {
        return "admin/banner/new";
    }
}
