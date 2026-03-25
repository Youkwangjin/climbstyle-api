package com.kwang.climbstyle.common.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {

    String upload(String subPath, String storedFilename, MultipartFile file) throws IOException;

    void delete(String fileUrl);
}
