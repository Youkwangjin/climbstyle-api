package com.kwang.climbstyle.domain.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity {

    private Integer adminNo;

    private String adminId;

    private String adminPassword;

    private String adminName;

    private String adminEmail;

    private String adminNickname;

    private String adminImageUrl;

    private LocalDateTime adminCreated;

    private LocalDateTime adminUpdated;

    private List<String> adminRoles;
}
