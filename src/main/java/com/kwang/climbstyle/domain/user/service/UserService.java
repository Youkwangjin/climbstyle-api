package com.kwang.climbstyle.domain.user.service;

import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.code.user.UserDeleteStatus;
import com.kwang.climbstyle.code.user.UserErrorCode;
import com.kwang.climbstyle.domain.file.service.FileService;
import com.kwang.climbstyle.domain.order.dto.response.OrderRecentResponse;
import com.kwang.climbstyle.domain.order.entity.OrderEntity;
import com.kwang.climbstyle.domain.order.repository.OrderRepository;
import com.kwang.climbstyle.domain.user.dto.request.*;
import com.kwang.climbstyle.domain.user.dto.response.UserProfileResponse;
import com.kwang.climbstyle.domain.user.entity.UserEntity;
import com.kwang.climbstyle.domain.user.repository.UserRepository;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FileService fileService;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

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
        final String userNm = request.getUserNm();
        final String userEmail = request.getUserEmail();
        final String userNickName = request.getUserNickName();
        final String userDeleteYn = UserDeleteStatus.ACTIVE.getCode();
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
                .userNm(userNm)
                .userEmail(userEmail)
                .userNickName(userNickName)
                .userDeleteYn(userDeleteYn)
                .userCreated(userCreated)
                .build();

        userRepository.insert(user);
    }

    public UserProfileResponse selectUserByNo(Integer userNo) {
        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userNm = data.getUserNm();
        final String userEmail = data.getUserEmail();
        final String userNickName = data.getUserNickName();
        final String userDeleteYn = data.getUserDeleteYn();
        final String userImageUrl = data.getUserImageUrl();
        final String userIntro = data.getUserIntro();
        final LocalDateTime userCreated = data.getUserCreated();
        final LocalDateTime userUpdated = data.getUserUpdated();
        final LocalDateTime userDeleted = data.getUserDeleted();

        List<OrderEntity> dataOrder = orderRepository.selectRecentOrdersByUserNo(userNo);

        List<OrderRecentResponse> orderResponses = dataOrder.stream()
                .map(order -> OrderRecentResponse.builder()
                        .orderNo(order.getOrderNo())
                        .orderTitle(order.getOrderTitle())
                        .orderStatus(order.getOrderStatus())
                        .orderCreated(order.getOrderCreated())
                        .build()
                ).toList();

        return UserProfileResponse.builder()
                .userNo(userNo)
                .userNm(userNm)
                .userEmail(userEmail)
                .userNickName(userNickName)
                .userDeleteYn(userDeleteYn)
                .userImgUrl(userImageUrl)
                .userIntro(userIntro)
                .userCreated(userCreated)
                .userUpdated(userUpdated)
                .userDeleted(userDeleted)
                .userOrders(orderResponses)
                .build();
    }

    @Transactional
    public void deactivateUser(Integer userNo) {
        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userDeleteYn = data.getUserDeleteYn();
        final LocalDateTime currentUserDeleted = data.getUserDeleted();
        final LocalDateTime userDeleted = LocalDateTime.now();
        final LocalDateTime now = LocalDateTime.now();
        final String userDeleteStatus = UserDeleteStatus.INACTIVE.getCode();

        if (StringUtils.equals(userDeleteYn, userDeleteStatus)) {
            throw new ClimbStyleException(UserErrorCode.USER_ALREADY_INACTIVE);
        }

        if (currentUserDeleted != null) {
            LocalDateTime availableAt = currentUserDeleted.plusDays(3);
            if (now.isBefore(availableAt)) {
                throw new ClimbStyleException(UserErrorCode.USER_DORMANCY_COOLDOWN);
            }
        }

        Boolean existOrderData = orderRepository.existsOrdersByUserNo(userNo);
        if (existOrderData) {
            throw new ClimbStyleException(UserErrorCode.USER_ORDER_EXISTS);
        }

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userDeleteYn(UserDeleteStatus.INACTIVE.getCode())
                .userDeleted(userDeleted)
                .build();

        userRepository.deactivateUser(userEntity);
    }

    @Transactional
    public void changePassword(Integer userNo, UserPasswordUpdateRequest request) {
        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String currentUserPassword = data.getUserPassword();
        final String userPassword = request.getUserPassword();
        if (!passwordEncoder.matches(userPassword, currentUserPassword)) {
            throw new ClimbStyleException(UserErrorCode.USER_PASSWORD_MISMATCH);
        }

        final String newUserPassword = passwordEncoder.encode(request.getNewUserPassword());
        final LocalDateTime userUpdated = LocalDateTime.now();

        UserEntity userEntity = UserEntity.builder()
                .userNo(userNo)
                .userPassword(newUserPassword)
                .userUpdated(userUpdated)
                .build();

        userRepository.updatePassword(userEntity);
    }

    @Transactional
    public void updateUser(Integer userNo, UserUpdateRequest request) {
        UserEntity data = userRepository.selectUserByNo(userNo);
        if (data == null) {
            throw new ClimbStyleException(UserErrorCode.USER_NOT_FOUND);
        }

        final String userDeleteYn = data.getUserDeleteYn();
        final String userDeleteStatus = UserDeleteStatus.INACTIVE.getCode();
        if (StringUtils.equals(userDeleteYn, userDeleteStatus)) {
            throw new ClimbStyleException(UserErrorCode.USER_INACTIVE_FORBIDDEN);
        }

        final String userNm = request.getUserNm();
        final String userNickName = request.getUserNickName();
        Boolean existNickName = userRepository.existUserNickName(userNickName);
        if (existNickName) {
            throw new ClimbStyleException(UserErrorCode.USER_NICKNAME_DUPLICATED);
        }

        final String userIntro = request.getUserIntro();
        final MultipartFile userProfileImg = request.getUserProfileImg();
        final LocalDateTime userUpdated = LocalDateTime.now();

        String userImageUrl = data.getUserImageUrl();
        if (userProfileImg != null && !userProfileImg.isEmpty()) {
            String oldUserImageUrl = data.getUserImageUrl();
            userImageUrl = fileService.fileUpload(userProfileImg, FileTypeCode.USER_PROFILE);

            if (oldUserImageUrl != null) {
                fileService.fileDelete(oldUserImageUrl);
            }
        }

        UserEntity user = UserEntity.builder()
                .userNo(userNo)
                .userNm(userNm)
                .userNickName(userNickName)
                .userImageUrl(userImageUrl)
                .userIntro(userIntro)
                .userUpdated(userUpdated)
                .build();

        userRepository.update(user);
    }
}
