package com.kwang.climbstyle.domain.feed.service;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.feed.dto.response.FeedListResponse;
import com.kwang.climbstyle.domain.feed.entity.FeedEntity;
import com.kwang.climbstyle.domain.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public List<FeedListResponse> getFeedList(CommonListRequest request) {
        request.setTotalCount(feedRepository.selectListCountByRequest(request));
        List<FeedEntity> list = feedRepository.selectFeedList(request);

        return list.stream()
                .map(feedList -> {
                    final Integer feedNo = feedList.getFeedNo();
                    final String feedTitle = feedList.getFeedTitle();
                    final LocalDateTime feedCreated = feedList.getFeedCreated();

                    return FeedListResponse.builder()
                            .feedNo(feedNo)
                            .feedTitle(feedTitle)
                            .feedCreated(feedCreated)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
