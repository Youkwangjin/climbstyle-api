package com.kwang.climbstyle.code.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FileTypeCode {
    USER_PROFILE(
            "profile/",
            new String[]{"jpg", "jpeg", "png", "gif"},
            5 * 1024 * 1024,
            "프로필 이미지"
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
    ;

    private final String subPath;

    private final String[] allowedExtensions;

    private final long maxFileSize;

    private final String description;

    public boolean isAllowedExtension(String extension) {
        return Arrays.asList(allowedExtensions).contains(extension.toLowerCase());
    }
}
