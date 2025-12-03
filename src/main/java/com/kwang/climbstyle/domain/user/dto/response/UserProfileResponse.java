package com.kwang.climbstyle.domain.user.dto.response;

import com.kwang.climbstyle.domain.order.dto.response.OrderRecentResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserProfileResponse {

    private Integer userNo;

    private String userNm;

    private String userNickName;

    private String userEmail;

    private String userDeleteYn;

    private String userImgUrl;

    private String userIntro;

    private LocalDateTime userCreated;

    private LocalDateTime userUpdated;

    private LocalDateTime userDeleted;

    private List<OrderRecentResponse> userOrders;
}
