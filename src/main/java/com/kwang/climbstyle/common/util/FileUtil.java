package com.kwang.climbstyle.common.util;

import com.kwang.climbstyle.code.file.FileErrorCode;
import com.kwang.climbstyle.exception.ClimbStyleException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtil {

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${profiles.name.local}")
    private String LOCAL;

    @Value("${profiles.name.prod}")
    private String PROD;

    public void upload(String directory, String fileName, MultipartFile multipartFile) throws IOException {
        if (StringUtils.equals(profile, LOCAL)) {
            File uploadPath = new File(directory);
            if (!uploadPath.exists()) {
                throw new ClimbStyleException(FileErrorCode.FILE_PATH_ERROR);
            }

            File uploadFile = new File(directory + fileName);
            multipartFile.transferTo(uploadFile);

        } else {
            throw new ClimbStyleException(FileErrorCode.PROFILE_NOT_FOUND);
        }
    }

    public void delete(String directory, String fileName) {
        if (StringUtils.equals(profile, LOCAL)) {
            final String filepath = directory + fileName;
            File file = new File(filepath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new ClimbStyleException(FileErrorCode.FILE_DELETE_ERROR);
                }

            } else {
                throw new ClimbStyleException(FileErrorCode.FILE_NOT_FOUND);
            }

        } else {
            throw new ClimbStyleException(FileErrorCode.PROFILE_NOT_FOUND);
        }
    }

    public byte[] download(String directory, String fileName) throws IOException {
        if (StringUtils.equals(profile, LOCAL)) {
            File file = new File(directory + fileName);
            if (!file.exists()) {
                throw new ClimbStyleException(FileErrorCode.FILE_NOT_FOUND);
            }

            return FileCopyUtils.copyToByteArray(file);

        } else {
            throw new ClimbStyleException(FileErrorCode.PROFILE_NOT_FOUND);
        }
    }
}
