package com.kwang.climbstyle.domain.feed.controller;

import com.kwang.climbstyle.code.feed.FeedSuccessCode;
import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCreateRequest;
import com.kwang.climbstyle.domain.feed.dto.response.FeedDetailResponse;
import com.kwang.climbstyle.domain.feed.dto.response.FeedLikeResponse;
import com.kwang.climbstyle.domain.feed.service.FeedService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedApiController {

    private final FeedService feedService;

    @GetMapping(value = "/api/v1/feeds/{feedNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedDetailResponse>> detailFeed(@PathVariable("feedNo") Integer feedNo) {
        FeedDetailResponse data = feedService.detailFeed(feedNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_DETAIL_SUCCESS, data);
    }

    @PostMapping(value = "/api/v1/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createFeed(@Valid FeedCreateRequest request) {
        feedService.createFeed(request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_CREATE_SUCCESS);
    }

    @PostMapping(value = "/api/v1/feed/{feedNo}/like", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedLikeResponse>> likeFeed(@PathVariable("feedNo") Integer feedNo) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        FeedLikeResponse data = feedService.likeFeed(feedNo, userNo);
        if (data.getIsLiked()) {
            return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIKE_CREATE_SUCCESS, data);
        } else {
            return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIKE_DELETE_SUCCESS, data);
        }
    }
}
