package com.kwang.climbstyle.domain.jwt.service;

import com.kwang.climbstyle.code.jwt.JwtErrorCode;
import com.kwang.climbstyle.code.jwt.JwtRefreshStatus;
import com.kwang.climbstyle.common.util.JWTUtil;
import com.kwang.climbstyle.domain.jwt.dto.response.JwtTokenResponse;
import com.kwang.climbstyle.domain.jwt.entity.JwtRefreshEntity;
import com.kwang.climbstyle.domain.jwt.repository.JwtRepository;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JWTUtil jwtUtil;

    private final JwtRepository jwtRepository;

    private final UserRepository userRepository;

    public Boolean existRefreshToken(String refreshToken) {
        return jwtRepository.existsByJwtRefreshToken(refreshToken);
    }

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

    @Transactional
    public JwtTokenResponse refreshRotate(String refreshToken) {
        if (refreshToken == null) {
            throw new ClimbStyleException(JwtErrorCode.REFRESH_TOKEN_MISSING);
        }

        if (!jwtUtil.isValidToken(refreshToken, false)) {
            throw new ClimbStyleException(JwtErrorCode.REFRESH_TOKEN_INVALID);
        }

        if (!existRefreshToken(refreshToken)) {
            throw new ClimbStyleException(JwtErrorCode.REFRESH_TOKEN_MISSING);
        }

        final Integer userNo = jwtUtil.getUserNo(refreshToken);
        final String username = jwtUtil.getUsername(refreshToken);
        final String userRole = jwtUtil.getRole(refreshToken);

        final String newAccessToken = jwtUtil.createJWT(userNo, username, userRole, true);
        final String newRefreshToken = jwtUtil.createJWT(userNo, username, userRole, false);

        rotateToken(userNo, newRefreshToken);

        return new JwtTokenResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void revokeToken(Integer userNo) {
        jwtRepository.revoke(userNo);
    }
}
