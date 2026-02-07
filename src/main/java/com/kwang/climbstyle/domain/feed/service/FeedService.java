package com.kwang.climbstyle.domain.feed.service;

import com.kwang.climbstyle.code.feed.FeedErrorCode;
import com.kwang.climbstyle.code.feed.FeedLikeVisibleStatus;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.feed.dto.request.FeedCreateRequest;
import com.kwang.climbstyle.domain.feed.dto.response.FeedCommentListResponse;
import com.kwang.climbstyle.domain.feed.dto.response.FeedDetailResponse;
import com.kwang.climbstyle.domain.feed.dto.response.FeedListResponse;
import com.kwang.climbstyle.domain.feed.entity.FeedEntity;
import com.kwang.climbstyle.domain.feed.entity.FeedFileEntity;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    private final FeedFileRepository feedFileRepository;

    private final FeedLikeRepository feedLikeRepository;

    private final FeedCommentRepository feedCommentRepository;

    private final FileService fileService;

    public List<FeedListResponse> getFeedList(CommonListRequest request) {
        request.setTotalCount(feedRepository.selectFeedListCountByRequest(request));
        return feedRepository.selectFeedList(request);
    }

    public List<FeedListResponse> getMyFeedList(CommonListRequest request, Integer userNo) {
        request.setTotalCount(feedRepository.selectMyFeedListCountByRequest(request, userNo));
        return feedRepository.selectMyFeedList(request, userNo);
    }

    public FeedDetailResponse detailFeed(Integer feedNo) {
        FeedDetailResponse feed = feedRepository.selectFeedByNo(feedNo);
        if (feed == null) {
            throw new ClimbStyleException(FeedErrorCode.FEED_NOT_FOUND);
        }

        List<String> images = feedFileRepository.selectFeedFilesByFeedNo(feedNo);
        if (images == null) {
            throw new ClimbStyleException(FeedErrorCode.FEED_FILE_NOT_FOUND);
        }
        feed.setFeedFilePaths(images);

        Integer feedLikeCount = feedLikeRepository.selectFeedLikeCountByFeedNo(feedNo);
        feed.setFeedLikeCount(feedLikeCount);

        List<FeedCommentListResponse> comments = feedCommentRepository.selectFeedCommentsByFeedNo(feedNo);
        feed.setFeedCommentList(comments);

        return feed;
    }

    @Transactional
    public void createFeed(FeedCreateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        final String feedTitle = request.getFeedTitle();
        final String feedContent = request.getFeedContent();
        final String feedVisibleYn = request.getFeedVisibleYn();
        final String feedLikeVisibleYn = FeedLikeVisibleStatus.VISIBLE.getCode();
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
}
