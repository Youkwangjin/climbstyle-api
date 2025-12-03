package com.kwang.climbstyle.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {

    private Integer orderItemNo;

    private Integer orderNo;

    private Integer productNo;

    private Integer productOptionNo;

    private Integer orderItemPrice;

    private Integer orderItemQty;
}
