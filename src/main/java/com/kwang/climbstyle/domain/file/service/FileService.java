package com.kwang.climbstyle.domain.file.service;

import com.kwang.climbstyle.code.file.FileErrorCode;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.common.util.FileUtil;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${profiles.name.local}")
    private String LOCAL;

    @Value("${profiles.name.prod}")
    private String PROD;

    @Value("${file.upload.base-path:}")
    private String baseUploadPath;

    @Value("${file.access.base-url:}")
    private String baseAccessUrl;

    private final FileUtil fileUtil;

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

        final String uploadPath = baseUploadPath + subPath;

        File directory = new File(uploadPath);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new ClimbStyleException(FileErrorCode.FILE_UPLOAD_ERROR);
        }

        try {
            if (StringUtils.equals(profile, LOCAL)) {
                fileUtil.upload(uploadPath, storedFilename, file);

            } else {
                throw new ClimbStyleException(FileErrorCode.PROFILE_NOT_FOUND);
            }

        } catch (IOException e) {
            throw new ClimbStyleException(FileErrorCode.FILE_UPLOAD_ERROR);
        }

        return baseAccessUrl + subPath + storedFilename;
    }

    public void fileDelete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        if (StringUtils.equals(profile, LOCAL)) {
            try {
                String relativePath = fileUrl.replace(baseAccessUrl, "");
                int lastSlashIndex = relativePath.lastIndexOf("/");
                String storedFilename = relativePath.substring(lastSlashIndex + 1);
                String subPath = relativePath.substring(0, lastSlashIndex + 1);
                String directory = baseUploadPath + subPath;
                fileUtil.delete(directory, storedFilename);

            } catch (ClimbStyleException ignored) {}

        } else {
            throw new ClimbStyleException(FileErrorCode.PROFILE_NOT_FOUND);
        }
    }

    private String generateDatePath() {
        final LocalDateTime now = LocalDateTime.now();
        return String.format("%d/%02d/%02d",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth());
    }
}
