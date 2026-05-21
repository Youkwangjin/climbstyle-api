package com.kwang.climbstyle.code.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 파일 타입 코드
 *
 * @author : Youkwangjin
 * @since : 2026-05-20
 * @version : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum FileTypeCode {
    USER_PROFILE(
            "profile/",
            new String[]{"jpg", "jpeg", "png", "gif"},
            5 * 1024 * 1024,
            "프로필 이미지"
    ),

    FEED_IMAGE(
            "feed/",
            new String[]{"jpg", "jpeg", "png"},
            5 * 1024 * 1024,
            "피드 이미지"
    ),

    EDITOR_IMAGE(
            "editor/",
            new String[]{"jpg", "jpeg", "png", "gif"},
            5 * 1024 * 1024,
            "에디터 이미지"
    ),

    DOCUMENT(
            "document/",
            new String[]{"pdf", "doc", "docx", "hwp"},
            10 * 1024 * 1024,
            "문서"
    ),

    INQUIRY(
            "inquiry/",
            new String[]{"pdf", "doc", "docx", "hwp"},
            10 * 1024 * 1024,
            "문의 첨부파일"
    ),

    BANNER(
            "banner/",
            new String[]{"jpg", "jpeg", "png"},
            5 * 1024 * 1024,
            "메인 배너 이미지"
    ),
    ;

    private final String subPath;

    private final String[] allowedExtensions;

    private final long maxFileSize;

    private final String description;

    public boolean isAllowedExtension(String extension) {
        return Arrays.asList(allowedExtensions).contains(extension.toLowerCase());
    }
}
