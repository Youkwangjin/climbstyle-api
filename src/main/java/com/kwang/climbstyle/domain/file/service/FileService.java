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

import java.io.IOException;
import java.util.UUID;

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

    public String fileUpload(MultipartFile file, FileTypeCode fileTypeCode) {
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

        String fileName = String.format("%s.%s",
                UUID.randomUUID().toString().replaceAll("-", ""),
                extension);
        String uploadPath = baseUploadPath + fileTypeCode.getSubPath();

        try {
            if (StringUtils.equals(profile, LOCAL)) {
                fileUtil.upload(uploadPath, fileName, file);

            } else {
                throw new ClimbStyleException(FileErrorCode.PROFILE_NOT_FOUND);
            }

        } catch (IOException e) {
            throw new ClimbStyleException(FileErrorCode.FILE_UPLOAD_ERROR);
        }

        return baseAccessUrl + fileTypeCode.getSubPath() + fileName;
    }

    public void fileDelete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        if (StringUtils.equals(profile, LOCAL)) {
            try {
                String relativePath = fileUrl.replace(baseAccessUrl, "");
                int lastSlashIndex = relativePath.lastIndexOf("/");
                String fileName = relativePath.substring(lastSlashIndex + 1);
                String subPath = relativePath.substring(0, lastSlashIndex + 1);
                String directory = baseUploadPath + subPath;
                fileUtil.delete(directory, fileName);

            } catch (ClimbStyleException ignored) {}

        } else {
            throw new ClimbStyleException(FileErrorCode.PROFILE_NOT_FOUND);
        }
    }
}
