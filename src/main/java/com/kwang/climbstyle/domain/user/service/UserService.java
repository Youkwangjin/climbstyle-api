package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.user.UserDeleteCode;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.domain.user.dto.request.UserCreateRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserEmailRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserIdRequest;
import com.kwang.climbstyle.domain.user.dto.request.UserNickNameRequest;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public void checkUserIdDuplicate(UserIdRequest request) {
        final String userId = request.getUserId();
        Boolean existId = userRepository.existUserId(userId);

        if (existId) {
            throw new ClimbStyleException(UserErrorCode.USER_ID_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public void checkUserEmailDuplicate(UserEmailRequest request) {
        final String userEmail = request.getUserEmail();
        Boolean existEmail = userRepository.existUserEmail(userEmail);

        if (existEmail) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public void checkUserNickNameDuplicate(UserNickNameRequest request) {
        final String userNickName = request.getUserNickName();
        Boolean existNickName = userRepository.existUserNickName(userNickName);

        if (existNickName) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }
    }

    @Transactional
    public void createUser(UserCreateRequest request) {
        final String userId = request.getUserId();
        final String userPassword = passwordEncoder.encode(request.getUserPassword());
        final String userEmail = request.getUserEmail();
        final String userNickName = request.getUserNickName();
        final String userDeleteYn = UserDeleteCode.ACTIVE.getCode();
        final LocalDateTime userCreated = LocalDateTime.now();

        Boolean existId = userRepository.existUserId(userId);
        if (existId) {
            throw new ClimbStyleException(UserErrorCode.USER_ID_DUPLICATED);
        }

        Boolean existEmail = userRepository.existUserEmail(userEmail);
        if (existEmail) {
            throw new ClimbStyleException(UserErrorCode.USER_EMAIL_DUPLICATED);
        }

        Boolean existNickName = userRepository.existUserNickName(userNickName);
        if (existNickName) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .userPassword(userPassword)
                .userEmail(userEmail)
                .userNickName(userNickName)
                .userDeleteYn(userDeleteYn)
                .userCreated(userCreated)
                .build();

        userRepository.insert(user);
    }
}
