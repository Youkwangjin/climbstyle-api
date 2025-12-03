package com.kwang.climbstyle.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderRecentResponse {

    private Integer orderNo;

    private String orderTitle;

    private Integer orderTotalPrice;

    private Integer orderStatus;

    private LocalDateTime orderCreated;
}
