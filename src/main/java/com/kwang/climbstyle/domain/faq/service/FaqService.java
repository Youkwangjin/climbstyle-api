package com.kwang.climbstyle.domain.faq.service;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.admin.dto.response.AdminFaqListResponse;
import com.kwang.climbstyle.domain.faq.dto.request.FaqCreateRequest;
import com.kwang.climbstyle.domain.faq.entity.FaqEntity;
import com.kwang.climbstyle.domain.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional(readOnly = true)
    public List<AdminFaqListResponse> getAdminFaqList(CommonListRequest request) {
        request.setTotalCount(faqRepository.selectAdminFaqListCountByRequest(request));

        return faqRepository.selectAdminFaqList(request);
    }

    @Transactional
    public void createFaq(FaqCreateRequest request) {
        final Integer adminNo = SecurityUtil.getCurrentAdminNo();
        final String faqQuestion = request.getFaqQuestion();
        final String faqAnswer = request.getFaqAnswer();
        final String faqVisibleYn = request.getFaqVisibleYn();
        final LocalDateTime faqCreated = LocalDateTime.now();

        FaqEntity faqEntity = FaqEntity.builder()
                .adminNo(adminNo)
                .faqQuestion(faqQuestion)
                .faqAnswer(faqAnswer)
                .faqVisibleYn(faqVisibleYn)
                .faqCreated(faqCreated)
                .build();

        faqRepository.insert(faqEntity);
    }
}
