package com.kwang.climbstyle.domain.faq.service;

import com.kwang.climbstyle.code.faq.FaqErrorCode;
import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.admin.dto.response.AdminFaqListResponse;
import com.kwang.climbstyle.domain.faq.dto.request.FaqCreateRequest;
import com.kwang.climbstyle.domain.faq.dto.request.FaqUpdateRequest;
import com.kwang.climbstyle.domain.faq.dto.response.FaqDetailResponse;
import com.kwang.climbstyle.domain.faq.dto.response.FaqListResponse;
import com.kwang.climbstyle.domain.faq.entity.FaqEntity;
import com.kwang.climbstyle.domain.faq.repository.FaqRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FAQ 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    /**
     * FAQ 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FaqListResponse> getFaqList(CommonListRequest request) {
        request.setTotalCount(faqRepository.selectFaqListCountByRequest(request));

        return faqRepository.selectFaqList(request);
    }

    /**
     * 관리자 FAQ 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminFaqListResponse> getAdminFaqList(CommonListRequest request) {
        request.setTotalCount(faqRepository.selectAdminFaqListCountByRequest(request));

        return faqRepository.selectAdminFaqList(request);
    }

    /**
     * 관리자 FAQ 상세 조회
     */
    @Transactional(readOnly = true)
    public FaqDetailResponse getAdminFaqDetail(Integer faqNo) {
        FaqDetailResponse faq = faqRepository.selectAdminFaqByNo(faqNo);
        if (faq == null) {
            throw new ClimbStyleException(FaqErrorCode.FAQ_NOT_FOUND);
        }

        return faq;
    }

    /**
     * FAQ 등록
     */
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

    /**
     * FAQ 수정
     */
    @Transactional
    public void updateFaq(Integer faqNo, FaqUpdateRequest request) {
        final String faqQuestion = request.getFaqQuestion();
        final String faqAnswer = request.getFaqAnswer();
        final String faqVisibleYn = request.getFaqVisibleYn();
        final LocalDateTime faqUpdated = LocalDateTime.now();

        FaqDetailResponse faq = faqRepository.selectAdminFaqByNo(faqNo);
        if (faq == null) {
            throw new ClimbStyleException(FaqErrorCode.FAQ_NOT_FOUND);
        }

        FaqEntity faqEntity = FaqEntity.builder()
                .faqNo(faqNo)
                .faqQuestion(faqQuestion)
                .faqAnswer(faqAnswer)
                .faqVisibleYn(faqVisibleYn)
                .faqUpdated(faqUpdated)
                .build();

        faqRepository.update(faqEntity);
    }

    /**
     * FAQ 삭제
     */
    @Transactional
    public void deleteFaq(Integer faqNo) {
        FaqDetailResponse faq = faqRepository.selectAdminFaqByNo(faqNo);
        if (faq == null) {
            throw new ClimbStyleException(FaqErrorCode.FAQ_NOT_FOUND);
        }

        faqRepository.delete(faqNo);
    }
}
