package com.kwang.climbstyle.domain.inquiry.service;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryListResponse;
import com.kwang.climbstyle.domain.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public List<InquiryListResponse> getInquiryList(CommonListRequest request) {
        Integer userNo = SecurityUtil.getCurrentUserNo();
        request.setTotalCount(inquiryRepository.selectInquiryListCountByRequest(request, userNo));

        return inquiryRepository.selectUserInquiryList(request, userNo);
    }
}
