package com.kwang.climbstyle.domain.feed.controller;

import com.kwang.climbstyle.code.feed.FeedSuccessCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCreateRequest;
import com.kwang.climbstyle.domain.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedApiController {

    private final FeedService feedService;

    @PostMapping(value = "/api/v1/feeds")
    public ResponseEntity<ApiSuccessResponse<Object>> createFeed(@Valid FeedCreateRequest request) {
        feedService.createFeed(request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_CREATE_SUCCESS);
    }
}
