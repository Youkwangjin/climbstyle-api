package com.kwang.climbstyle.domain.inquiry.service;

import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.code.inquiry.InquiryDeleteCode;
import com.kwang.climbstyle.code.inquiry.InquiryErrorCode;
import com.kwang.climbstyle.code.inquiry.InquiryStatusCode;
import com.kwang.climbstyle.common.protocal.CommonListRequest;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.admin.dto.request.AdminInquiryListRequest;
import com.kwang.climbstyle.domain.admin.dto.response.AdminInquiryListResponse;
import com.kwang.climbstyle.domain.file.service.FileService;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryAnswerRequest;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryCreateRequest;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryStatusRequest;
import com.kwang.climbstyle.domain.inquiry.dto.request.InquiryUpdateRequest;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryDetailResponse;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryFileResponse;
import com.kwang.climbstyle.domain.inquiry.dto.response.InquiryListResponse;
import com.kwang.climbstyle.domain.inquiry.entity.InquiryEntity;
import com.kwang.climbstyle.domain.inquiry.entity.InquiryFileEntity;
import com.kwang.climbstyle.domain.inquiry.repository.InquiryFileRepository;
import com.kwang.climbstyle.domain.inquiry.repository.InquiryRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final FileService fileService;

    private final InquiryRepository inquiryRepository;

    private final InquiryFileRepository inquiryFileRepository;

    public List<InquiryListResponse> getInquiryList(CommonListRequest request) {
        Integer userNo = SecurityUtil.getCurrentUserNo();
        request.setTotalCount(inquiryRepository.selectInquiryListCountByRequest(request, userNo));

        return inquiryRepository.selectUserInquiryList(request, userNo);
    }

    public List<AdminInquiryListResponse> getAdminInquiryList(AdminInquiryListRequest request) {
        request.setTotalCount(inquiryRepository.selectAdminInquiryListCountByRequest(request));

        return inquiryRepository.selectAdminInquiryList(request);
    }

    public InquiryDetailResponse getInquiryDetail(Integer inquiryNo) {
        Integer userNo = SecurityUtil.getCurrentUserNo();
        InquiryDetailResponse inquiry = inquiryRepository.selectInquiryByNo(inquiryNo, userNo);
        if (inquiry == null) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_FOUND);
        }

        return inquiry;
    }

    public InquiryDetailResponse getAdminInquiryDetail(Integer inquiryNo) {
        InquiryDetailResponse inquiry = inquiryRepository.selectAdminInquiryByNo(inquiryNo);
        if (inquiry == null) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_FOUND);
        }

        return inquiry;
    }

    @Transactional
    public void createInquiry(InquiryCreateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        final String inquiryTitle = request.getInquiryTitle();
        final String inquiryContent = request.getInquiryContent();
        final String inquiryStatus = InquiryStatusCode.PENDING.getCode();
        final String inquiryDeleteYn = InquiryDeleteCode.NOT_DELETED.getCode();
        final LocalDateTime inquiryCreated = LocalDateTime.now();
        final List<MultipartFile> inquiryFiles = request.getInquiryFiles();

        InquiryEntity inquiryEntity = InquiryEntity.builder()
                .userNo(userNo)
                .inquiryTitle(inquiryTitle)
                .inquiryContent(inquiryContent)
                .inquiryStatus(inquiryStatus)
                .inquiryDeleteYn(inquiryDeleteYn)
                .inquiryCreated(inquiryCreated)
                .build();

        inquiryRepository.insert(inquiryEntity);

        final Integer inquiryNo = inquiryEntity.getInquiryNo();

        if (inquiryFiles != null && !inquiryFiles.isEmpty()) {
            for (MultipartFile inquiryFile : inquiryFiles) {
                final String inquiryFileOriginalName = FilenameUtils.getName(inquiryFile.getOriginalFilename());
                final String inquiryFileExtnsNm = FilenameUtils.getExtension(inquiryFileOriginalName);
                final String inquiryFileStoredName = String.format("%d_%s.%s",
                        inquiryNo,
                        UUID.randomUUID().toString().replaceAll("-", ""),
                        inquiryFileExtnsNm);
                final String inquiryFilePath = fileService.fileUpload(inquiryFile, FileTypeCode.INQUIRY, inquiryFileStoredName);
                final String inquiryFileSize = String.valueOf(inquiryFile.getSize());
                final LocalDateTime inquiryFileCreated = LocalDateTime.now();

                InquiryFileEntity inquiryFileEntity = InquiryFileEntity.builder()
                        .inquiryNo(inquiryNo)
                        .inquiryFileOriginalName(inquiryFileOriginalName)
                        .inquiryFileStoredName(inquiryFileStoredName)
                        .inquiryFilePath(inquiryFilePath)
                        .inquiryFileExtnsNm(inquiryFileExtnsNm)
                        .inquiryFileSize(inquiryFileSize)
                        .inquiryFileCreated(inquiryFileCreated)
                        .build();

                inquiryFileRepository.insert(inquiryFileEntity);
            }
        }
    }

    @Transactional
    public void updateInquiry(Integer inquiryNo, InquiryUpdateRequest request) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        final String inquiryTitle = request.getInquiryTitle();
        final String inquiryContent = request.getInquiryContent();
        final String inquiryStatus = InquiryStatusCode.PENDING.getCode();
        final LocalDateTime inquiryUpdated = LocalDateTime.now();
        final List<MultipartFile> inquiryFiles = request.getInquiryFiles();

        InquiryDetailResponse inquiry = inquiryRepository.selectInquiryByNo(inquiryNo, userNo);
        if (inquiry == null) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_FOUND);
        }

        final String currentInquiryStatus = inquiry.getInquiryStatus();
        if (!StringUtils.equals(inquiryStatus, currentInquiryStatus)) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_MODIFIABLE);
        }

        InquiryEntity inquiryEntity = InquiryEntity.builder()
                .inquiryNo(inquiryNo)
                .inquiryTitle(inquiryTitle)
                .inquiryContent(inquiryContent)
                .inquiryUpdated(inquiryUpdated)
                .build();

        inquiryRepository.update(inquiryEntity);

        List<Integer> inquiryFilesNo = request.getInquiryFileIds();
        if (inquiryFilesNo == null) {
            inquiryFilesNo = new ArrayList<>();
        }

        List<InquiryFileResponse> existingFiles = inquiryFileRepository.selectInquiryFileByInquiryNo(inquiryNo);
        List<Integer> deleteFilesNo = new ArrayList<>();
        for (InquiryFileResponse inquiryFile : existingFiles) {
            if (!inquiryFilesNo.contains(inquiryFile.getInquiryFileNo())) {
                deleteFilesNo.add(inquiryFile.getInquiryFileNo());
            }
        }

        for (Integer inquiryFileNo : deleteFilesNo) {
            InquiryFileResponse inquiryFile = inquiryFileRepository.selectInquiryFileByNo(inquiryFileNo);
            if (inquiryFile == null) {
                throw new ClimbStyleException(InquiryErrorCode.INQUIRY_FILE_NOT_FOUND);
            }

            final String inquiryFilePath = inquiryFile.getInquiryFilePath();

            inquiryFileRepository.delete(inquiryFileNo);
            fileService.fileDelete(inquiryFilePath);
        }

        if (inquiryFiles != null && !inquiryFiles.isEmpty()) {
            for (MultipartFile inquiryFile : inquiryFiles) {
                final String inquiryFileOriginalName = FilenameUtils.getName(inquiryFile.getOriginalFilename());
                final String inquiryFileExtnsNm = FilenameUtils.getExtension(inquiryFileOriginalName);
                final String inquiryFileStoredName = String.format("%d_%s.%s",
                        inquiryNo,
                        UUID.randomUUID().toString().replaceAll("-", ""),
                        inquiryFileExtnsNm);
                final String inquiryFilePath = fileService.fileUpload(inquiryFile, FileTypeCode.INQUIRY, inquiryFileStoredName);
                final String inquiryFileSize = String.valueOf(inquiryFile.getSize());
                final LocalDateTime inquiryFileCreated = LocalDateTime.now();

                InquiryFileEntity inquiryFileEntity = InquiryFileEntity.builder()
                        .inquiryNo(inquiryNo)
                        .inquiryFileOriginalName(inquiryFileOriginalName)
                        .inquiryFileStoredName(inquiryFileStoredName)
                        .inquiryFilePath(inquiryFilePath)
                        .inquiryFileExtnsNm(inquiryFileExtnsNm)
                        .inquiryFileSize(inquiryFileSize)
                        .inquiryFileCreated(inquiryFileCreated)
                        .build();

                inquiryFileRepository.insert(inquiryFileEntity);
            }
        }
    }

    @Transactional
    public void updateInquiryStatus(Integer inquiryNo, InquiryStatusRequest request) {
        final String inquiryStatus = request.getInquiryStatus();

        InquiryDetailResponse inquiry = inquiryRepository.selectAdminInquiryByNo(inquiryNo);
        if (inquiry == null) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_FOUND);
        }

        final String currentInquiryDeleteYn = inquiry.getInquiryDeleteYn();
        final String currentInquiryStatus = inquiry.getInquiryStatus();

        if (StringUtils.equals(currentInquiryDeleteYn, InquiryDeleteCode.DELETED.getCode())) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_ALREADY_DELETED);
        }

        if (StringUtils.equals(currentInquiryStatus, InquiryStatusCode.COMPLETED.getCode())) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_STATUS_NOT_CHANGEABLE);
        }

        InquiryEntity inquiryEntity = InquiryEntity.builder()
                .inquiryNo(inquiryNo)
                .inquiryStatus(inquiryStatus)
                .build();

        inquiryRepository.updateStatus(inquiryEntity);
    }

    @Transactional
    public void saveInquiryAnswer(Integer inquiryNo, InquiryAnswerRequest request) {
        final Integer adminNo = SecurityUtil.getCurrentAdminNo();
        final String inquiryAnswerContent =  request.getInquiryAnswerContent();
        final String inquiryAnswerContentText = Jsoup.parse(inquiryAnswerContent).text();
        final String inquiryStatus = InquiryStatusCode.COMPLETED.getCode();

        InquiryDetailResponse inquiry = inquiryRepository.selectAdminInquiryByNo(inquiryNo);
        if (inquiry == null) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_FOUND);
        }

        final String currentInquiryDeleteYn = inquiry.getInquiryDeleteYn();
        final String currentInquiryStatus = inquiry.getInquiryStatus();

        if (StringUtils.equals(currentInquiryDeleteYn, InquiryDeleteCode.DELETED.getCode())) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_ALREADY_DELETED);
        }

        if (!StringUtils.equals(currentInquiryStatus, InquiryStatusCode.PROGRESS.getCode())) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_ANSWER_NOT_ALLOWED);
        }

        InquiryEntity inquiryEntity = InquiryEntity.builder()
                .inquiryNo(inquiryNo)
                .adminNo(adminNo)
                .inquiryStatus(inquiryStatus)
                .inquiryAnswerContent(inquiryAnswerContent)
                .inquiryAnswerContentText(inquiryAnswerContentText)
                .build();

        inquiryRepository.saveAnswer(inquiryEntity);
    }

    @Transactional
    public void deleteInquiry(Integer inquiryNo) {
        final Integer userNo = SecurityUtil.getCurrentUserNo();
        final String inquiryStatus = InquiryStatusCode.PROGRESS.getCode();
        final String inquiryDeleteYn = InquiryDeleteCode.DELETED.getCode();
        final LocalDateTime inquiryUpdated = LocalDateTime.now();

        InquiryDetailResponse inquiry = inquiryRepository.selectInquiryByNo(inquiryNo, userNo);
        if (inquiry == null) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_FOUND);
        }

        final String currentInquiryStatus = inquiry.getInquiryStatus();
        if (StringUtils.equals(inquiryStatus, currentInquiryStatus)) {
            throw new ClimbStyleException(InquiryErrorCode.INQUIRY_NOT_DELETABLE);
        }

        InquiryEntity inquiryEntity = InquiryEntity.builder()
                .inquiryNo(inquiryNo)
                .inquiryDeleteYn(inquiryDeleteYn)
                .inquiryUpdated(inquiryUpdated)
                .build();

        inquiryRepository.delete(inquiryEntity);

        List<InquiryFileResponse> files = inquiryFileRepository.selectInquiryFileByInquiryNo(inquiryNo);
        for (InquiryFileResponse inquiryFile : files) {

            final Integer inquiryFileNo = inquiryFile.getInquiryFileNo();
            final String inquiryFilePath = inquiryFile.getInquiryFilePath();

            inquiryFileRepository.delete(inquiryFileNo);
            fileService.fileDelete(inquiryFilePath);
        }
    }
}
