package com.kwang.climbstyle.domain.file.service;

import com.kwang.climbstyle.code.file.FileErrorCode;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.common.file.FileStorage;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 파일 서비스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorage fileStorage;

    /**
     * 파일 업로드
     */
    public String fileUpload(MultipartFile file, FileTypeCode fileTypeCode, String storedFilename) {
        if (file.isEmpty()) {
            throw new ClimbStyleException(FileErrorCode.FILE_EMPTY);
        }

        if (file.getSize() > fileTypeCode.getMaxFileSize()) {
            throw new ClimbStyleException(FileErrorCode.FILE_SIZE_EXCEEDED);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ClimbStyleException(FileErrorCode.FILE_INVALID_NAME);
        }

        String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();
        if (!fileTypeCode.isAllowedExtension(extension)) {
            throw new ClimbStyleException(FileErrorCode.FILE_INVALID_TYPE);
        }

        String datePath = this.generateDatePath();
        String subPath = fileTypeCode.getSubPath() + datePath + "/";

        try {
            return fileStorage.upload(subPath, storedFilename, file);
        } catch (IOException e) {
            throw new ClimbStyleException(FileErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 파일 삭제
     */
    public void fileDelete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        fileStorage.delete(fileUrl);
    }

    /**
     * 날짜 기반 저장 경로 생성 (yyyy/MM/dd)
     */
    private String generateDatePath() {
        final LocalDateTime now = LocalDateTime.now();
        return String.format("%d/%02d/%02d",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth());
    }
}
