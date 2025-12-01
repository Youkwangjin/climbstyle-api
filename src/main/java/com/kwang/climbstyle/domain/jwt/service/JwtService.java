package com.kwang.climbstyle.domain.jwt.service;

import com.kwang.climbstyle.code.jwt.JwtRefreshStatus;
import com.kwang.climbstyle.domain.jwt.entity.JwtRefreshEntity;
import com.kwang.climbstyle.domain.jwt.repository.JwtRepository;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtRepository jwtRepository;

    private final UserRepository userRepository;

    @Transactional
    public void rotateToken(Integer userNo, String jwtRefreshToken) {
        jwtRepository.revoke(userNo);

        JwtRefreshEntity jwtRefreshEntity = JwtRefreshEntity.builder()
                .userNo(userNo)
                .jwtRefreshToken(jwtRefreshToken)
                .jwtRefreshTokenCreated(LocalDateTime.now())
                .jwtRefreshTokenExpires(LocalDateTime.now().plusDays(7))
                .jwtRefreshTokenRevokedYn(JwtRefreshStatus.ACTIVE.getCode())
                .build();

        jwtRepository.insert(jwtRefreshEntity);
    }
}
