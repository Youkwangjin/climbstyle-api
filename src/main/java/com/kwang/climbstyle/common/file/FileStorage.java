package com.kwang.climbstyle.common.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 파일 스토리지 인터페이스
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
public interface FileStorage {

    String upload(String subPath, String storedFilename, MultipartFile file) throws IOException;

    void delete(String fileUrl);
}
