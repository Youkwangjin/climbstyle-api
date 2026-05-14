package com.kwang.climbstyle.domain.feed.controller;

import com.kwang.climbstyle.code.feed.FeedSuccessCode;
import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.response.ApiResponseBuilder;
import com.kwang.climbstyle.common.response.ApiSuccessResponse;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCommentCreateRequest;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCreateRequest;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCursorRequest;
import com.kwang.climbstyle.domain.feed.dto.request.FeedUpdateRequest;
import com.kwang.climbstyle.domain.feed.dto.response.FeedCursorResponse;
import com.kwang.climbstyle.domain.feed.dto.response.FeedDetailResponse;
import com.kwang.climbstyle.domain.feed.dto.response.FeedLikeDetailResponse;
import java.util.List;
import com.kwang.climbstyle.domain.feed.dto.response.FeedLikeResponse;
import com.kwang.climbstyle.domain.feed.service.FeedService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FeedApiController {

    private final FeedService feedService;

    @GetMapping(value = "/api/v1/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedCursorResponse>> getFeedList(FeedCursorRequest request) {
        FeedCursorResponse data = feedService.getFeedListByCursor(request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIST_SUCCESS, data);
    }

    @GetMapping(value = "/api/v1/feeds/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedCursorResponse>> getMyFeedList(FeedCursorRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        FeedCursorResponse data = feedService.getMyFeedListByCursor(request, userNo);
        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIST_SUCCESS, data);
    }

    @GetMapping(value = "/api/v1/feeds/{feedNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedDetailResponse>> detailFeed(@PathVariable("feedNo") Integer feedNo) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        FeedDetailResponse data = feedService.detailFeed(feedNo, userNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_DETAIL_SUCCESS, data);
    }

    @GetMapping(value = "/api/v1/feeds/{feedNo}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<List<FeedLikeDetailResponse>>> likesFeed(@PathVariable("feedNo") Integer feedNo) {
        List<FeedLikeDetailResponse> data = feedService.detailFeedLike(feedNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIKE_DETAIL_SUCCESS, data);
    }

    @PostMapping(value = "/api/v1/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createFeed(@Valid FeedCreateRequest request) {
        feedService.createFeed(request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_CREATE_SUCCESS);
    }

    @PostMapping(value = "/api/v1/feeds/{feedNo}/like", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/api/v1/feeds/{feedNo}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> commentFeed(@PathVariable("feedNo") Integer feedNo,
                                                                  @Valid @RequestBody FeedCommentCreateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.UNAUTHORIZED_ERROR);
        }

        feedService.commentFeed(userNo, feedNo, request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_COMMENT_CREATE_SUCCESS);
    }

    @PatchMapping(value = "/api/v1/feeds/{feedNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateFeed(@PathVariable ("feedNo") Integer feedNo,
                                                                 @Valid @RequestBody FeedUpdateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        feedService.updateFeed(userNo, feedNo, request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_UPDATE_SUCCESS);
    }

    @DeleteMapping(value = "/api/v1/feeds/{feedNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deleteFeed(@PathVariable("feedNo") Integer feedNo) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        feedService.deleteFeed(userNo, feedNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_DELETE_SUCCESS);
    }
}
