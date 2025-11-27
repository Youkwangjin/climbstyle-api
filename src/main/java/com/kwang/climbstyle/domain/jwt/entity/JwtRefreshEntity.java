package com.kwang.climbstyle.domain.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRefreshEntity {

    private Integer jwtRefreshNo;

    private Integer userNo;

    private String jwtRefreshToken;

    private LocalDateTime jwtRefreshTokenCreated;

    private LocalDateTime jwtRefreshTokenExpires;

    private String jwtRefreshTokenRevokedYn;
}
