package com.kwang.climbstyle.domain.admin.controller;

import com.kwang.climbstyle.code.banner.BannerSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.banner.dto.request.BannerCreateRequest;
import com.kwang.climbstyle.domain.banner.dto.request.BannerUpdateRequest;
import com.kwang.climbstyle.domain.banner.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminBannerApiController {

    private final BannerService bannerService;

    @PostMapping(value = "/api/v1/banners", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createBanner(BannerCreateRequest request) {
        bannerService.createBanner(request);

        return ApiResponseBuilder.ok(BannerSuccessCode.BANNER_CREATE_SUCCESS);
    }

    @PatchMapping(value = "/api/v1/banners/{bannerNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateBanner(@PathVariable("bannerNo") Integer bannerNo,
                                                                   @RequestBody @Valid BannerUpdateRequest request) {
        bannerService.updateBanner(bannerNo, request);

        return ApiResponseBuilder.ok(BannerSuccessCode.BANNER_UPDATE_SUCCESS);
    }

    @DeleteMapping(value = "/api/v1/banners/{bannerNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deleteBanner(@PathVariable("bannerNo") Integer bannerNo) {
        bannerService.deleteBanner(bannerNo);

        return ApiResponseBuilder.ok(BannerSuccessCode.BANNER_DELETE_SUCCESS);
    }
}
