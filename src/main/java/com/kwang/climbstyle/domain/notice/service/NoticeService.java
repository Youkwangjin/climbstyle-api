package com.kwang.climbstyle.domain.notice.service;

import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.code.notice.NoticeErrorCode;
import com.kwang.climbstyle.code.notice.NoticeVisibleStatus;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.admin.dto.response.AdminNoticeListResponse;
import com.kwang.climbstyle.domain.file.service.FileService;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeCreateRequest;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeListRequest;
import com.kwang.climbstyle.domain.notice.dto.request.NoticeUpdateRequest;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeDetailResponse;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeFileResponse;
import com.kwang.climbstyle.domain.notice.dto.response.NoticeListResponse;
import com.kwang.climbstyle.domain.notice.entity.NoticeEntity;
import com.kwang.climbstyle.domain.notice.entity.NoticeFileEntity;
import com.kwang.climbstyle.domain.notice.repository.NoticeFileRepository;
import com.kwang.climbstyle.domain.notice.repository.NoticeRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
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
public class NoticeService {

    private final FileService fileService;

    private final NoticeRepository noticeRepository;

    private final NoticeFileRepository noticeFileRepository;

    public List<NoticeListResponse> getNoticeList(NoticeListRequest request) {
        request.setTotalCount(noticeRepository.selectNoticeListCountByRequest(request));

        List<NoticeListResponse> noticeList = noticeRepository.selectNoticeList(request);

        LocalDateTime sevenDaysAgo =  LocalDateTime.now().minusDays(7);
        for (NoticeListResponse notice : noticeList) {
            notice.setNoticeNew(notice.getNoticeCreated().isAfter(sevenDaysAgo));
        }

        return noticeList;
    }

    public List<AdminNoticeListResponse> getAdminNoticeList(NoticeListRequest request) {
        request.setTotalCount(noticeRepository.selectAdminNoticeListCountByRequest(request));

        List<AdminNoticeListResponse> adminNoticeList = noticeRepository.selectAdminNoticeList(request);

        LocalDateTime sevenDaysAgo =  LocalDateTime.now().minusDays(7);
        for (AdminNoticeListResponse notice : adminNoticeList) {
            notice.setNoticeNew(notice.getNoticeCreated().isAfter(sevenDaysAgo));
        }

        return adminNoticeList;
    }

    public NoticeDetailResponse getNoticeDetail(Integer noticeNo) {
        NoticeDetailResponse notice = noticeRepository.selectNoticeByNo(noticeNo);
        if (notice == null) {
            throw new ClimbStyleException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }

        notice.setPrevNotice(noticeRepository.selectPrevNotice(noticeNo));
        notice.setNextNotice(noticeRepository.selectNextNotice(noticeNo));

        noticeRepository.updateNoticeHit(noticeNo);
        notice.setNoticeHit(notice.getNoticeHit() + 1);

        return notice;
    }

    public NoticeDetailResponse getAdminNoticeDetail(Integer noticeNo) {
        NoticeDetailResponse notice = noticeRepository.selectAdminNoticeByNo(noticeNo);
        if (notice == null) {
            throw new ClimbStyleException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }

        notice.setPrevNotice(noticeRepository.selectPrevNotice(noticeNo));
        notice.setNextNotice(noticeRepository.selectNextNotice(noticeNo));

        noticeRepository.updateNoticeHit(noticeNo);
        notice.setNoticeHit(notice.getNoticeHit() + 1);

        return notice;
    }

    @Transactional
    public void createNotice(NoticeCreateRequest request) {
        final Integer adminNo = SecurityUtil.getCurrentAdminNo();
        final String noticeCategory = request.getNoticeCategory();
        final String noticeTitle = request.getNoticeTitle();
        final String noticeContent = request.getNoticeContent();
        final String noticeContentText = Jsoup.parse(noticeContent).text();
        final String noticePinYn = request.getNoticePinYn();
        final String noticeVisibleYn = NoticeVisibleStatus.VISIBLE.getCode();
        final List<MultipartFile> noticeFiles = request.getNoticeFiles();
        final LocalDateTime noticeCreated = LocalDateTime.now();

        NoticeEntity noticeEntity = NoticeEntity.builder()
                .adminNo(adminNo)
                .noticeCategory(noticeCategory)
                .noticeTitle(noticeTitle)
                .noticeContent(noticeContent)
                .noticeContentText(noticeContentText)
                .noticeHit(0)
                .noticePinYn(noticePinYn)
                .noticeVisibleYn(noticeVisibleYn)
                .noticeCreated(noticeCreated)
                .build();

        noticeRepository.insert(noticeEntity);

        final Integer noticeNo = noticeEntity.getNoticeNo();

        if (noticeFiles != null && !noticeFiles.isEmpty()) {
            for (MultipartFile noticeFile : noticeFiles) {
                final String noticeFileOriginalName = FilenameUtils.getName(noticeFile.getOriginalFilename());
                final String noticeFileExtnsNm = FilenameUtils.getExtension(noticeFileOriginalName);
                final String noticeFileStoredName = String.format("%d_%s.%s",
                        noticeNo,
                        UUID.randomUUID().toString().replaceAll("-", ""),
                        noticeFileExtnsNm);
                final String noticeFilePath = fileService.fileUpload(noticeFile, FileTypeCode.DOCUMENT, noticeFileStoredName);
                final String noticeFileSize = String.valueOf(noticeFile.getSize());
                final LocalDateTime noticeFileCreated = LocalDateTime.now();

                NoticeFileEntity noticeFileEntity = NoticeFileEntity.builder()
                        .noticeNo(noticeNo)
                        .noticeFileOriginalName(noticeFileOriginalName)
                        .noticeFileStoredName(noticeFileStoredName)
                        .noticeFilePath(noticeFilePath)
                        .noticeFileExtnsNm(noticeFileExtnsNm)
                        .noticeFileSize(noticeFileSize)
                        .noticeFileCreated(noticeFileCreated)
                        .build();

                noticeFileRepository.insert(noticeFileEntity);
            }
        }
    }

    @Transactional
    public void updateNotice(NoticeUpdateRequest request, Integer noticeNo) {
        final String noticeCategory = request.getNoticeCategory();
        final String noticeTitle = request.getNoticeTitle();
        final String noticeContent = request.getNoticeContent();
        final String noticeContentText = Jsoup.parse(noticeContent).text();
        final String noticePinYn = request.getNoticePinYn();
        final String noticeVisibleYn = request.getNoticeVisibleYn();
        final List<MultipartFile> noticeFiles = request.getNoticeFiles();
        final LocalDateTime noticeUpdated = LocalDateTime.now();

        NoticeDetailResponse notice = noticeRepository.selectAdminNoticeByNo(noticeNo);
        if (notice == null) {
            throw new ClimbStyleException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }

        NoticeEntity noticeEntity = NoticeEntity.builder()
                .noticeNo(noticeNo)
                .noticeCategory(noticeCategory)
                .noticeTitle(noticeTitle)
                .noticeContent(noticeContent)
                .noticeContentText(noticeContentText)
                .noticePinYn(noticePinYn)
                .noticeVisibleYn(noticeVisibleYn)
                .noticeUpdated(noticeUpdated)
                .build();

        noticeRepository.update(noticeEntity);

        List<Integer> noticeFilesNo = request.getNoticeFileIds();
        if (noticeFilesNo == null) {
            noticeFilesNo = new ArrayList<>();
        }

        List<NoticeFileResponse> existingFiles = noticeFileRepository.selectNoticeFileByNoticeNo(noticeNo);
        List<Integer> deleteFilesNo = new ArrayList<>();
        for (NoticeFileResponse noticeFile : existingFiles) {
            if (!noticeFilesNo.contains(noticeFile.getNoticeFileNo())) {
                deleteFilesNo.add(noticeFile.getNoticeFileNo());
            }
        }

        for (Integer noticeFileNo : deleteFilesNo) {
            NoticeFileResponse noticeFile = noticeFileRepository.selectNoticeFileByNo(noticeFileNo);
            if (noticeFile == null) {
                throw new ClimbStyleException(NoticeErrorCode.NOTICE_FILE_NOT_FOUND);
            }

            final String noticeFilePath = noticeFile.getNoticeFilePath();

            fileService.fileDelete(noticeFilePath);
            noticeFileRepository.delete(noticeFileNo);
        }

        if (noticeFiles != null && !noticeFiles.isEmpty()) {
            for (MultipartFile noticeFile : noticeFiles) {
                final String noticeFileOriginalName = FilenameUtils.getName(noticeFile.getOriginalFilename());
                final String noticeFileExtnsNm = FilenameUtils.getExtension(noticeFileOriginalName);
                final String noticeFileStoredName = String.format("%d_%s.%s",
                        noticeNo,
                        UUID.randomUUID().toString().replaceAll("-", ""),
                        noticeFileExtnsNm);
                final String noticeFilePath = fileService.fileUpload(noticeFile, FileTypeCode.DOCUMENT, noticeFileStoredName);
                final String noticeFileSize = String.valueOf(noticeFile.getSize());
                final LocalDateTime noticeFileCreated = LocalDateTime.now();

                NoticeFileEntity noticeFileEntity = NoticeFileEntity.builder()
                        .noticeNo(noticeNo)
                        .noticeFileOriginalName(noticeFileOriginalName)
                        .noticeFileStoredName(noticeFileStoredName)
                        .noticeFilePath(noticeFilePath)
                        .noticeFileExtnsNm(noticeFileExtnsNm)
                        .noticeFileSize(noticeFileSize)
                        .noticeFileCreated(noticeFileCreated)
                        .build();

                noticeFileRepository.insert(noticeFileEntity);
            }
        }
    }

    @Transactional
    public void deleteNotice(Integer noticeNo) {
        NoticeDetailResponse notice = noticeRepository.selectAdminNoticeByNo(noticeNo);
        if (notice == null) {
            throw new ClimbStyleException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }

        List<NoticeFileResponse> files = noticeFileRepository.selectNoticeFileByNoticeNo(noticeNo);
        for (NoticeFileResponse noticeFile : files) {
            fileService.fileDelete(noticeFile.getNoticeFilePath());
        }

        noticeRepository.delete(noticeNo);
    }
}
