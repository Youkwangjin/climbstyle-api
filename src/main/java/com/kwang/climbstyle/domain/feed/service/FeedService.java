package com.kwang.climbstyle.domain.feed.service;

import com.kwang.climbstyle.code.feed.FeedErrorCode;
import com.kwang.climbstyle.code.feed.FeedVisibleStatus;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.code.http.HttpErrorCode;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCommentCreateRequest;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCreateRequest;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCursorRequest;
import com.kwang.climbstyle.domain.feed.dto.request.FeedUpdateRequest;
import com.kwang.climbstyle.domain.feed.dto.response.*;
import com.kwang.climbstyle.domain.feed.entity.FeedCommentEntity;
import com.kwang.climbstyle.domain.feed.entity.FeedEntity;
import com.kwang.climbstyle.domain.feed.entity.FeedFileEntity;
import com.kwang.climbstyle.domain.feed.entity.FeedLikeEntity;
import com.kwang.climbstyle.domain.feed.repository.FeedCommentRepository;
import com.kwang.climbstyle.domain.feed.repository.FeedFileRepository;
import com.kwang.climbstyle.domain.feed.repository.FeedLikeRepository;
import com.kwang.climbstyle.domain.feed.repository.FeedRepository;
import com.kwang.climbstyle.domain.file.service.FileService;

import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 피드 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-16
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    private final FeedFileRepository feedFileRepository;

    private final FeedLikeRepository feedLikeRepository;

    private final FeedCommentRepository feedCommentRepository;

    private final FileService fileService;

    /**
     * 최신 피드 목록 조회 (홈 화면용)
     */
    @Transactional(readOnly = true)
    public List<FeedListResponse> getLatestFeeds(int limit, Integer userNo) {
        return feedRepository.selectLatestFeeds(limit, userNo);
    }

    /**
     * 피드 목록 조회 (커서 기반)
     */
    @Transactional(readOnly = true)
    public FeedCursorResponse getFeedListByCursor(FeedCursorRequest request, Integer userNo) {
        final Integer cursor = request.getCursor();
        final int size = request.getSize();

        List<FeedListResponse> feeds = feedRepository.selectFeedListByCursor(cursor, size, userNo);
        final boolean hasNext = feeds.size() == size;

        Integer nextCursor = null;
        if (hasNext) {
            nextCursor = feeds.get(feeds.size() - 1).getFeedNo();
        }

        return FeedCursorResponse.builder()
                .feeds(feeds)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    /**
     * 내 피드 목록 조회 (커서 기반)
     */
    @Transactional(readOnly = true)
    public FeedCursorResponse getMyFeedListByCursor(FeedCursorRequest request, Integer userNo) {
        final Integer cursor = request.getCursor();
        final int size = request.getSize();

        List<FeedListResponse> feeds = feedRepository.selectMyFeedListByCursor(cursor, size, userNo);
        final boolean hasNext = feeds.size() == size;

        Integer nextCursor = null;
        if (hasNext) {
            nextCursor = feeds.get(feeds.size() - 1).getFeedNo();
        }

        return FeedCursorResponse.builder()
                .feeds(feeds)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    /**
     * 피드 상세 조회
     */
    @Transactional(readOnly = true)
    public FeedDetailResponse detailFeed(Integer feedNo, Integer userNo) {
        FeedDetailResponse feed = feedRepository.selectFeedByNo(feedNo);
        if (feed == null) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        feed.setIsAuthor(Objects.equals(feed.getUserNo(), userNo));

        if (userNo != null) {
            Boolean isLiked = feedLikeRepository.existFeedLikeByFeedNoAndUserNo(feedNo, userNo);
            feed.setIsLiked(isLiked != null && isLiked);
        } else {
            feed.setIsLiked(false);
        }

        List<String> feedFilePaths = feedFileRepository.selectFeedFilePathsByFeedNo(feedNo);
        if (feedFilePaths == null) {
            throw new ClimbStyleException(FeedErrorCode.FEED_FILE_NOT_FOUND);
        }
        feed.setFeedFilePaths(feedFilePaths);

        final String feedLikeVisibleYn = FeedVisibleStatus.HIDDEN.getCode();
        final String currentLikeVisibleYn = feed.getFeedLikeVisibleYn();
        if (!Objects.equals(currentLikeVisibleYn, feedLikeVisibleYn)) {
            Integer feedLikeCount = feedLikeRepository.selectFeedLikeCountByFeedNo(feedNo);
            feed.setFeedLikeCount(feedLikeCount);
        }

        Integer feedCommentCount = feedCommentRepository.selectFeedCommentsCountByFeedNo(feedNo);
        feed.setFeedCommentCount(feedCommentCount);

        List<FeedCommentListResponse> comments = feedCommentRepository.selectFeedCommentsByFeedNo(feedNo);
        feed.setFeedCommentList(comments);

        return feed;
    }

    /**
     * 피드 좋아요 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FeedLikeDetailResponse> detailFeedLike(Integer feedNo) {
        FeedDetailResponse feed = feedRepository.selectFeedByNo(feedNo);
        if (feed == null) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        if (Objects.equals(feed.getFeedLikeVisibleYn(), FeedVisibleStatus.HIDDEN.getCode())) {
            throw new ClimbStyleException(FeedErrorCode.FEED_LIKE_HIDDEN);
        }

        return feedLikeRepository.selectFeedLikeListByNo(feedNo);
    }

    /**
     * 피드 등록
     */
    @Transactional
    public void createFeed(FeedCreateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        final String feedTitle = request.getFeedTitle();
        final String feedContent = request.getFeedContent();
        final String feedVisibleYn = request.getFeedVisibleYn();
        final String feedLikeVisibleYn = FeedVisibleStatus.VISIBLE.getCode();
        final List<MultipartFile> feedImages = request.getFeedImages();
        final LocalDateTime feedCreated = LocalDateTime.now();

        FeedEntity feedEntity = FeedEntity.builder()
                .userNo(userNo)
                .feedTitle(feedTitle)
                .feedContent(feedContent)
                .feedVisibleYn(feedVisibleYn)
                .feedLikeVisibleYn(feedLikeVisibleYn)
                .feedCreated(feedCreated)
                .build();

        feedRepository.insert(feedEntity);
        final Integer feedNo = feedEntity.getFeedNo();

        if (feedImages != null && !feedImages.isEmpty()) {
            for (int i = 0; i < feedImages.size(); i++) {
                MultipartFile multipartFile = feedImages.get(i);

                final String feedFileOriginalName = FilenameUtils.getName(multipartFile.getOriginalFilename());
                final String feedFileExtnsNm = FilenameUtils.getExtension(feedFileOriginalName);
                final String feedFileStoredName = String.format("%d_%d_%s.%s", feedNo, userNo, UUID.randomUUID().toString().replaceAll("-", ""), feedFileExtnsNm);
                final String feedFilePath = fileService.fileUpload(multipartFile, FileTypeCode.FEED_IMAGE, feedFileStoredName);
                final String feedFileSize = String.valueOf(multipartFile.getSize());
                final Integer feedFileSortOrder = i + 1;
                final LocalDateTime feedFileCreated = LocalDateTime.now();

                FeedFileEntity feedFileEntity = FeedFileEntity.builder()
                        .feedNo(feedNo)
                        .feedFileOriginalName(feedFileOriginalName)
                        .feedFileStoredName(feedFileStoredName)
                        .feedFilePath(feedFilePath)
                        .feedFileExtnsNm(feedFileExtnsNm)
                        .feedFileSize(feedFileSize)
                        .feedFileSortOrder(feedFileSortOrder)
                        .feedFileCreated(feedFileCreated)
                        .build();

                feedFileRepository.insert(feedFileEntity);
            }
        }
    }

    /**
     * 피드 좋아요 / 좋아요 취소
     */
    @Transactional
    public FeedLikeResponse likeFeed(Integer feedNo, Integer userNo) {
        if (!feedRepository.existFeedByNo(feedNo)) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        Boolean isLike = feedLikeRepository.existFeedLikeByFeedNoAndUserNo(feedNo, userNo);
        if (isLike) {
            feedLikeRepository.delete(feedNo, userNo);
        } else {
            FeedLikeEntity feedLikeEntity = FeedLikeEntity.builder()
                    .feedNo(feedNo)
                    .userNo(userNo)
                    .feedLikeCreated(LocalDateTime.now())
                    .build();

            Integer inserted = feedLikeRepository.insert(feedLikeEntity);
            if (inserted == null || inserted == 0) {
                isLike = true;
            }
        }

        final Integer feedLikeCount = feedLikeRepository.selectFeedLikeCountByFeedNo(feedNo);

        return FeedLikeResponse.builder()
                .isLiked(!isLike)
                .feedLikeCount(feedLikeCount)
                .build();
    }

    /**
     * 피드 댓글 등록
     */
    @Transactional
    public void commentFeed(Integer userNo, Integer feedNo, FeedCommentCreateRequest request) {
        final String feedCommentContent = request.getFeedCommentContent();
        final Integer feedCommentNo = request.getFeedCommentParentNo();

        if (!feedRepository.existFeedByNo(feedNo)) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        if (feedCommentNo != null) {
            if (!feedCommentRepository.existsByFeedCommentNo(feedCommentNo)) {
                throw new ClimbStyleException(FeedErrorCode.FEED_COMMENT_NOT_FOUND);
            }
        }

        final LocalDateTime feedCreated = LocalDateTime.now();
        final String feedCommentVisibleYn = FeedVisibleStatus.VISIBLE.getCode();

        FeedCommentEntity feedCommentEntity = FeedCommentEntity.builder()
                .feedNo(feedNo)
                .userNo(userNo)
                .feedCommentParentNo(feedCommentNo)
                .feedCommentContent(feedCommentContent)
                .feedCommentVisibleYn(feedCommentVisibleYn)
                .feedCommentCreated(feedCreated)
                .build();

        feedCommentRepository.insert(feedCommentEntity);
    }

    /**
     * 피드 수정
     */
    @Transactional
    public void updateFeed(Integer userNo, Integer feedNo, FeedUpdateRequest request) {
        final String feedTitle = request.getFeedTitle();
        final String feedContent = request.getFeedContent();
        final String feedVisibleYn = request.getFeedVisibleYn();
        final String feedLikeVisibleYn = request.getFeedLikeVisibleYn();
        final LocalDateTime feedUpdated = LocalDateTime.now();

        if (!feedRepository.existFeedByNo(feedNo)) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        if (!feedRepository.existsFeedByNoAndUserNo(feedNo, userNo)) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        FeedEntity feedEntity = FeedEntity.builder()
                .feedNo(feedNo)
                .feedTitle(feedTitle)
                .feedContent(feedContent)
                .feedVisibleYn(feedVisibleYn)
                .feedLikeVisibleYn(feedLikeVisibleYn)
                .feedUpdated(feedUpdated)
                .build();

        feedRepository.update(feedEntity);
    }

    /**
     * 피드 삭제
     */
    @Transactional
    public void deleteFeed(Integer userNo, Integer feedNo) {
        if (!feedRepository.existFeedByNo(feedNo)) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        if (!feedRepository.existsFeedByNoAndUserNo(feedNo, userNo)) {
            throw new ClimbStyleException(HttpErrorCode.FORBIDDEN_ERROR);
        }

        List<String> filePaths = feedFileRepository.selectFeedFilePathsByFeedNo(feedNo);

        feedRepository.delete(feedNo);

        for (String filePath : filePaths) {
            fileService.fileDelete(filePath);
        }
    }

    /**
     * 피드 댓글 삭제
     */
    @Transactional
    public void deleteComment(Integer userNo, Integer feedNo, Integer feedCommentNo) {
        FeedDetailResponse feed = feedRepository.selectFeedByNo(feedNo);
        if (feed == null) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        if (!feedCommentRepository.existsByFeedCommentNo(feedCommentNo)) {
            throw new ClimbStyleException(FeedErrorCode.FEED_COMMENT_NOT_FOUND);
        }

        feedCommentRepository.deleteByFeedCommentNoAndUserNo(feedCommentNo, userNo);
        feedCommentRepository.deleteRepliesByParentCommentNo(feedCommentNo);
    }
}
