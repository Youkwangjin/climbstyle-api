package com.kwang.climbstyle.domain.faq.service;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminFaqListResponse;
import com.kwang.climbstyle.domain.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
