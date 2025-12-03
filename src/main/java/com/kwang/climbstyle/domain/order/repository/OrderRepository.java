package com.kwang.climbstyle.domain.order.repository;

import com.kwang.climbstyle.domain.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderRepository {

    List<OrderEntity> selectRecentOrdersByUserNo(@Param("userNo") Integer userNo);
}
