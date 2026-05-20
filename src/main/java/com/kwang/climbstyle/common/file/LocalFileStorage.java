package com.kwang.climbstyle.common.file;

import com.kwang.climbstyle.code.file.FileErrorCode;
import com.kwang.climbstyle.exception.ClimbStyleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 로컬 파일 스토리지 구현체 (local 프로파일)
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Component
@Profile("local")
public class LocalFileStorage implements FileStorage {

    @Value("${file.upload.base-path}")
    private String localUploadPath;

    @Value("${file.access.base-url}")
    private String localAccessUrl;

    @Override
    public String upload(String subPath, String storedFilename, MultipartFile file) throws IOException {
        String uploadPath = localUploadPath + subPath;

        File directory = new File(uploadPath);
        if(!directory.exists() && !directory.mkdirs()) {
            throw new ClimbStyleException(FileErrorCode.FILE_UPLOAD_ERROR);
        }

        File uploadFile = new File(uploadPath + storedFilename);
        file.transferTo(uploadFile);

        return localAccessUrl + subPath + storedFilename;
    }

    @Override
    public void delete(String fileUrl) {
        String relativePath = fileUrl.replace(localAccessUrl, "");
        int lastSlashIndex = relativePath.lastIndexOf("/");
        String subPath = relativePath.substring(0, lastSlashIndex + 1);
        String storedFilename = relativePath.substring(lastSlashIndex + 1);

        String filePath = localUploadPath + subPath + storedFilename;
        File file = new File(filePath);

        if (file.exists()) {
            if (!file.delete()) {
                throw new ClimbStyleException(FileErrorCode.FILE_DELETE_ERROR);
            }
        } else {
            throw new ClimbStyleException(FileErrorCode.FILE_NOT_FOUND);
        }
    }
}
