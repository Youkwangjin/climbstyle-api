package com.kwang.climbstyle.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    private Integer orderNo;

    private Integer userNo;

    private String orderName;

    private String orderTel;

    private String orderTitle;

    private Integer orderTotalPrice;

    private Integer orderStatus;

    private LocalDateTime orderCreated;

    private LocalDateTime orderUpdated;
}
