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

/**
 * 피드 API 컨트롤러
 *
 * @author : Youkwangjin
 * @since : 2026-05-16
 * @version : 1.0
 */
@RestController
@RequiredArgsConstructor
public class FeedApiController {

    private final FeedService feedService;

    /**
     * 피드 목록 조회
     * [ClimbStyle] > [피드]
     */
    @GetMapping(value = "/api/v1/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedCursorResponse>> getFeedList(FeedCursorRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        FeedCursorResponse data = feedService.getFeedListByCursor(request, userNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIST_SUCCESS, data);
    }

    /**
     * 내 피드 목록 조회
     * [ClimbStyle] > [마이페이지] > 내 피드
     */
    @GetMapping(value = "/api/v1/feeds/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedCursorResponse>> getMyFeedList(FeedCursorRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        if (userNo == null) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        FeedCursorResponse data = feedService.getMyFeedListByCursor(request, userNo);
        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIST_SUCCESS, data);
    }

    /**
     * 피드 상세 조회
     * [ClimbStyle] > [피드] > 피드 상세
     */
    @GetMapping(value = "/api/v1/feeds/{feedNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<FeedDetailResponse>> detailFeed(@PathVariable("feedNo") Integer feedNo) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        FeedDetailResponse data = feedService.detailFeed(feedNo, userNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_DETAIL_SUCCESS, data);
    }

    /**
     * 피드 좋아요 사용자 목록 조회
     * [ClimbStyle] > [피드] > 피드 상세 > 좋아요 목록
     */
    @GetMapping(value = "/api/v1/feeds/{feedNo}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<List<FeedLikeDetailResponse>>> likesFeed(@PathVariable("feedNo") Integer feedNo) {
        List<FeedLikeDetailResponse> data = feedService.detailFeedLike(feedNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_LIKE_DETAIL_SUCCESS, data);
    }

    /**
     * 피드 등록
     * [ClimbStyle] > [피드] > 피드 작성
     */
    @PostMapping(value = "/api/v1/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createFeed(@Valid FeedCreateRequest request) {
        feedService.createFeed(request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_CREATE_SUCCESS);
    }

    /**
     * 피드 좋아요 / 좋아요 취소
     * [ClimbStyle] > [피드] > 피드 상세 > 좋아요
     */
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

    /**
     * 피드 댓글 등록
     * [ClimbStyle] > [피드] > 피드 상세 > 댓글 작성
     */
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

    /**
     * 피드 수정
     * [ClimbStyle] > [피드] > 피드 상세 > 피드 수정
     */
    @PatchMapping(value = "/api/v1/feeds/{feedNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateFeed(@PathVariable ("feedNo") Integer feedNo,
                                                                 @Valid @RequestBody FeedUpdateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        feedService.updateFeed(userNo, feedNo, request);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_UPDATE_SUCCESS);
    }

    /**
     * 피드 삭제
     * [ClimbStyle] > [피드] > 피드 상세 > 피드 삭제
     */
    @DeleteMapping(value = "/api/v1/feeds/{feedNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> deleteFeed(@PathVariable("feedNo") Integer feedNo) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        feedService.deleteFeed(userNo, feedNo);

        return ApiResponseBuilder.ok(FeedSuccessCode.FEED_DELETE_SUCCESS);
    }
}
