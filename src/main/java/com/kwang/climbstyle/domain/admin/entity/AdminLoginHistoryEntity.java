package com.kwang.climbstyle.domain.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginHistoryEntity {

    private Integer adminLoginHistoryNo;

    private Integer adminNo;

    private String adminLoginHistoryFailId;

    private String adminLoginHistorySuccessYn;

    private String adminLoginHistoryIp;

    private String adminLoginHistoryUserAgent;

    private LocalDateTime adminLoginHistoryAt;
}
